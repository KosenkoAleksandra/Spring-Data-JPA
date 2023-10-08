package ru.skypro.lessons.springboot.springboot.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeFullInfo> getAllInfo();
    EmployeeDTO addEmployee(Employee employee);
    EmployeeDTO getEmployeeById(int id);
    void deleteEmployeeById(int id);
    List<EmployeeDTO> getEmployeesByName(String name);
    EmployeeFullInfo getEmployeeByIdFullInfo(Integer id);
    EmployeeDTO employeeWithHighestSalary();
    List<EmployeeDTO> allEmployeesByPosition(String name);
    List<Employee> getEmployeeWithPaging(int pageNum, int pageSize);
    void upload(MultipartFile employees);
    void addBatchEmployees(List<EmployeeDTO> employees);
}
