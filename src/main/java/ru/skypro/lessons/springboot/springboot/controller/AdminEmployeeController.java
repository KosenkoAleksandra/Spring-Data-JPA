package ru.skypro.lessons.springboot.springboot.controller;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;

import java.util.List;

@Data
@RestController
@RequestMapping("/admin/employees")
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public EmployeeDTO addEmployee(@RequestBody Employee employee) {
        return EmployeeDTO.fromEmployee(employeeService.addEmployee(employee).toEmployee());
    }

    @PostMapping("/all")
    public void addBatchEmployees(List<EmployeeDTO> employees) {
        employeeService.addBatchEmployees(employees);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestPart("employees") MultipartFile employees) {
        employeeService.upload(employees);
    }
}
