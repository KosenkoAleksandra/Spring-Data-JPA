package ru.skypro.lessons.springboot.springboot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Position;
import ru.skypro.lessons.springboot.springboot.entity.Report;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.PositionRepository;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;
import ru.skypro.lessons.springboot.springboot.service.ReportService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReportIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ReportRepository reportRepository;

    @BeforeEach
    void cleanData() {
        employeeRepository.deleteAll();
        reportRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createReport() throws Exception {
        createMockEmployees();
        mockMvc.perform(post("/report"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void getReportById() throws Exception {
        createMockEmployees();
        Integer id = reportService.createReport();
        MvcResult result = mockMvc.perform(get("/report/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        byte[] resourceContent = result.getResponse().getContentAsByteArray();
        Report report = reportRepository.findById(id).orElse(null);
        String file = report.getReport();
        assertThat(resourceContent).isNotEmpty();
        assertThat(resourceContent).containsExactly(file.getBytes());
    }

    void createMockEmployees() {
        Position position = new Position(1, "Position-1");
        Position position2 = new Position(2, "Position-2");
        positionRepository.save(position);
        positionRepository.save(position2);
        List<Employee> employeeList = List.of(
                new Employee(1, "Employee-1", 10000, position),
                new Employee(2, "Employee-2", 20000, position2),
                new Employee(3, "Employee-3", 30000, position2)
        );
        employeeRepository.saveAll(employeeList);
    }
}
