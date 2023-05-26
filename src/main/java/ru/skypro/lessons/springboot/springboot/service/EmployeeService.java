package ru.skypro.lessons.springboot.springboot.service;

import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeFullInfo> getAllInfo();
    EmployeeDTO addEmployee(Employee employee);
    EmployeeDTO getEmployeeById(int id);
    void deleteEmployeeById(int id);
    List<EmployeeDTO> getEmployeesByName(String name);
    EmployeeDTO employeeWithHighestSalary();
    List<EmployeeDTO> allEmployeesPosition();

}
