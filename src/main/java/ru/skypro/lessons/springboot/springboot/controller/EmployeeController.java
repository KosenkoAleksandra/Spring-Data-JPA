package ru.skypro.lessons.springboot.springboot.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Position;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;

import java.util.List;
import java.util.Map;

@Data
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;


    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    @PostMapping
    public EmployeeDTO addEmployee(@RequestBody Employee employee) {
        return EmployeeDTO.fromEmployee(employeeService.addEmployee(employee).toEmployee());
    }
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeByID(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }
    @GetMapping("/{name}")
    public List<EmployeeDTO> getEmployeesByName(@PathVariable String name) {
        return employeeService.getEmployeesByName(name);
    }
    @GetMapping("/{id}/fullInfo")
    public List<EmployeeFullInfo> getAllInfo(@PathVariable Integer id) {
        return employeeService.getAllInfo();
    }
    @GetMapping("/withHighestSalary")
    public EmployeeDTO employeeWithHighestSalary(@PathVariable Integer id) {
        return employeeService.employeeWithHighestSalary();
    }
    @GetMapping
    public List<EmployeeDTO> allEmployeesPosition (@RequestParam("position") Position position) {
        return null;
    }
}
