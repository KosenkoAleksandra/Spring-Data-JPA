package ru.skypro.lessons.springboot.springboot.controller;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/{name}")
    public List<EmployeeDTO> getEmployeesByName(@PathVariable String name) {
        return employeeService.getEmployeesByName(name);
    }
    @GetMapping("/withHighestSalary")
    public EmployeeDTO employeeWithHighestSalary(@PathVariable Integer id) {
        return employeeService.employeeWithHighestSalary();
    }
    @GetMapping
    public List<EmployeeDTO> allEmployeesByPosition (@RequestParam("position") String name) {
        return employeeService.allEmployeesByPosition(name);
    }
    @GetMapping("/page")
    public List<Employee> getEmployeeWithPaging(@RequestParam("page")int pageIndex, int unitPerPage) {
        return employeeService.getEmployeeWithPaging(pageIndex, unitPerPage);
    }


}
