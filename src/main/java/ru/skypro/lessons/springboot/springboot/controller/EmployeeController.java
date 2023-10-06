package ru.skypro.lessons.springboot.springboot.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;

import java.util.List;

@Data
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeByID(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/by-name/{name}")
    public List<EmployeeDTO> getEmployeesByName(@PathVariable String name) {
        return employeeService.getEmployeesByName(name);
    }

    @GetMapping("/by-position/{position}")
    public List<EmployeeDTO> allEmployeesByPosition(@PathVariable String position) {
        return employeeService.allEmployeesByPosition(position);
    }

    @GetMapping("/withHighestSalary")
    public EmployeeDTO employeeWithHighestSalary() {
        return employeeService.employeeWithHighestSalary();
    }

    @GetMapping("/page")
    public List<Employee> getEmployeeWithPaging(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        return employeeService.getEmployeeWithPaging(pageNum, pageSize);
    }

    @GetMapping("/{id}/fullInfo")
    public EmployeeFullInfo getEmployeeByIdFullInfo(@PathVariable Integer id) {
        return employeeService.getEmployeeByIdFullInfo(id);
    }
}
