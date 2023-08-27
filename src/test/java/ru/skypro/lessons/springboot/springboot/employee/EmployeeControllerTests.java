package ru.skypro.lessons.springboot.springboot.employee;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WithMockUser
@ActiveProfiles ("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @BeforeEach
    void cleanData() {
        employeeRepository.deleteAll();
    }

    @Test
    public void uploadFileTest() throws Exception {
        List<EmployeeDTO> expected = objectMapper.readValue(
                EmployeeControllerTests.class.getResourceAsStream("employees.json"),
                new TypeReference<>() {
                });
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "employees",
                "employees.json",
                MediaType.APPLICATION_JSON_VALUE,
                EmployeeControllerTests.class.getResourceAsStream("employees.json")
        );
        mockMvc.perform(multipart("/admin/employees/upload")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(result -> {
                    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    List<EmployeeDTO> actual = getAllEmployeesTest();
                    Assertions.assertThat(actual)
                            .hasSize(3)
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                            .containsExactlyInAnyOrderElementsOf(expected);
                });

    }

    @Test
    public void employeeWithHighestSalaryTest() throws Exception {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 1000))
                .limit(5)
                .collect(Collectors.toList());

        List<EmployeeDTO> employeeDTOS = employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        add(employeeDTOS);
        EmployeeDTO expected = employeeDTOS.stream()
                .max(Comparator.comparingInt(EmployeeDTO::getSalary))
                .get();

        mockMvc.perform(get("/employees/withHighestSalary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    Assertions.assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDTO.class))
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(expected);
                });

    }


    @Test
    public void addBatchEmployeesTest() throws Exception {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 1000))
                .limit(5)
                .collect(Collectors.toList());

        List<EmployeeDTO> employeeDTOS = employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        add(employeeDTOS);
    }

    public List<EmployeeDTO> getAllEmployeesTest() throws Exception {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        mockMvc.perform(get("/employees/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    employeeDTOS.addAll(
                            objectMapper.readValue(
                                    result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    }
                            )
                    );
                });
        return employeeDTOS;
    }

    private Employee generateEmployee(int id, Integer positionId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setSalary(faker.random().nextInt(12500, 250000));
        employee.setName(faker.name().fullName());
        employee.setPosition(positionId != null ? generatePosition(positionId) : null);
        return employee;
    }
    private Position generatePosition(int id) {
        Position position = new Position();
        position.setId(id);
        position.setName(faker.company().profession());
        return position;
    }
    private EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSalary(employee.getSalary())
                .setPosition(employee.getPosition());
    }

    private void add(List<EmployeeDTO> employeeDTOS) throws Exception {
        mockMvc.perform(post("/admin/employees/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTOS)))
                .andExpect(result -> {
                    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    List<EmployeeDTO> actual = getAllEmployeesTest();
                    Assertions.assertThat(actual)
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                            .containsExactlyInAnyOrderElementsOf(employeeDTOS);
                });
    }
}
