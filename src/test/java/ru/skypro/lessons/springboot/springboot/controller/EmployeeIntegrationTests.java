package ru.skypro.lessons.springboot.springboot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.PositionDTO;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.PositionRepository;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @BeforeEach
    void cleanData() {
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getAllEmployeesTest() throws Exception {
        Employee employee = generateEmployee();
        Employee employee1 = generateEmployee();

        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(employee.getName()))
                .andExpect(jsonPath("$[1].name").value(employee1.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeeByIdTest() throws Exception {
        Employee employee = generateEmployee();

        mockMvc.perform(get("/employees/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employee.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeeByName() throws Exception {
        Employee employee = generateEmployee();

        mockMvc.perform(get("/employees/by-name/" + employee.getName())
                .param("name", employee.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(employee.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeeByPosition() throws Exception {
        Employee employee = generateEmployee();

        mockMvc.perform(get("/employees/by-position/" + employee.getPosition().getName())
                .param("position", String.valueOf(employee.getPosition().getName())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(employee.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeeWithHighestSalary() throws Exception {
        Employee minSalaryEmployee = generateEmployee(100);
        Employee maxSalaryEmployee = generateEmployee(100_000);

        mockMvc.perform(get("/employees/withHighestSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(maxSalaryEmployee.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeesPageTest() throws Exception {
        Employee employee = generateEmployee();
        Employee employee1 = generateEmployee();
        Employee employee3 = generateEmployee();

        mockMvc.perform(get("/employees/page?pageNum=0&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(employee.getName()))
                .andExpect(jsonPath("$[1].name").value(employee1.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void addEmployeeTest() throws Exception {
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toDTO(generateEmployee()))))
                .andExpect(result -> {
                    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteEmployee() throws Exception {
        Employee employee = generateEmployee();

        mockMvc.perform(delete("/admin/employees/{id}", employee.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getEmployeeByIdFullInfo() throws Exception {
        Employee employee = generateEmployee();

        mockMvc.perform(get("/employees/{id}/fullInfo", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employee.getName()));
    }

//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void givenNoUsersInDatabase_whenUserAdded_thenStatusOK() throws Exception {
//
//        Position position = positionRepository.save(new Position(1, "position_1"));
//
//        JSONObject positionDto = new JSONObject();
//        positionDto.put("id", position.getId());
//        positionDto.put("name", position.getName());
//
//        JSONObject employee = new JSONObject();
//        employee.put("name", "Ivan");
//        employee.put("salary", 100000);
//        employee.put("positionDTO", positionDto);
//
//        mockMvc.perform(post("/admin/employees")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(employee.toString()))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/employees/{id}", 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Ivan"));
//    }
//
//
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void whenGetEmployees_thenEmptyJsonArray() throws Exception {
//        mockMvc.perform(get("/employees/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$").isEmpty());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getSumSalary() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/salary/sum"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNumber())
//                .andExpect(jsonPath("$").value(60000));
//    }
//
//    void addEmployeeListInRepository() {
//        Position position = new Position(1, "position-1");
//        Position position2 = new Position(2, "position-2");
//        positionRepository.save(position);
//        positionRepository.save(position2);
//        List<Employee> employeeList = List.of(
//                new Employee(1, "Ivan", 10000, position),
//                new Employee(2, "Inna", 20000, position2),
//                new Employee(3, "Anna", 30000, position2)
//        );
//        employeeRepository.saveAll(employeeList);
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getEmployeeWithMaxSalary() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/salary/max"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Anna"))
//                .andExpect(jsonPath("$.salary").value(30000));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getEmployeeWithMinSalary() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/salary/min"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Ivan"))
//                .andExpect(jsonPath("$.salary").value(10000));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getEmployeesWithSalaryAboveAverage() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/high-salary"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].name").value("Anna"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getEmployeesWithSalaryHigherThan() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/salary/higher")
//                .param("compareSalary", String.valueOf(25000)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].name").value("Anna"));
//    }
//

//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void getEmployeeByPage() throws Exception {
//        addEmployeeListInRepository();
//        mockMvc.perform(get("/employees/page")
//                .param("page", String.valueOf(0)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(3));
//    }
//
//
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void editEmployee() throws Exception {
//        addEmployeeListInRepository();
//        JSONObject employee = new JSONObject();
//        employee.put("id", 1);
//        employee.put("name", "Ilya");
//        employee.put("salary", 10000);
//        employee.put("department", 1);
//        JSONObject position = new JSONObject();
//        position.put("id", 1);
//        position.put("name", "position-1");
//        employee.put("positionDTO", position);
//
//        mockMvc.perform(put("/admin/employees/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(employee.toString()))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/employees/{id}", 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Ilya"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void addEmployeeFromFile() throws Exception {
//        addEmployeeListInRepository();
//        EmployeeDTO employeeDTO = new EmployeeDTO(4, "Petr", 40000, new PositionDTO(2, "position-2"));
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(employeeDTO);
//        MockMultipartFile file = new MockMultipartFile("file", "employee.json", MediaType.MULTIPART_FORM_DATA_VALUE, json.getBytes());
//
//        mockMvc.perform(multipart("/admin/employees/upload")
//                .file(file))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/employees/{id}", 4))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Petr"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void whenBDIsEmpty_getStatus404() throws Exception {
////        проверка get методов на выбрасывание исключения и возвращение 404 статуса
//        mockMvc.perform(get("/employees/salary/max"))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/employees/salary/min"))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/employees/high-salary"))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/employees/withHighestSalary"))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/employees/{id}", 1))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/employees/{id}/fullInfo", 1))
//                .andExpect(status().isBadRequest());
//        //        проверка delete, edit методов на выбрасывание исключения и возвращение 404 статуса
//        mockMvc.perform(delete("/admin/employees/{id}", 1))
//                .andExpect(status().isBadRequest());
//        JSONObject employee = new JSONObject();
//        employee.put("id", 1);
//        employee.put("name", "Ilya");
//        employee.put("salary", 10000);
//        employee.put("department", 1);
//        JSONObject position = new JSONObject();
//        position.put("id", 1);
//        position.put("name", "position-1");
//        employee.put("positionDTO", position);
//        mockMvc.perform(put("/admin/employees/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(employee.toString()))
//                .andExpect(status().isBadRequest());
//
//    }

    private Employee generateEmployee(int salary) {
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setName(checkStringField(faker.name().firstName()));

        Position position = generatePosition();

        employee.setPosition(position);

        positionRepository.save(position);
        employeeRepository.save(employee);

        return employee;
    }

    private Employee generateEmployee() {
        return generateEmployee(faker.random().nextInt(12500, 250000));
    }

    private Position generatePosition() {
        Position position = new Position();
        position.setName(checkStringField(faker.company().profession()));
        return position;
    }

    private String checkStringField(String value) {
        if (value.length() > 16) {
            return value.substring(0, 14);
        }

        return value;
    }

    private EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSalary(employee.getSalary())
                .setPositionDTO(PositionDTO.fromPosition(employee.getPosition()));
    }
}
