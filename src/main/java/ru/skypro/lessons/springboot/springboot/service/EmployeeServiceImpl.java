package ru.skypro.lessons.springboot.springboot.service;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    Pageable employee = PageRequest.of(0, 10);

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAllEmployees().stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO addEmployee(Employee employee) {
        return EmployeeDTO.fromEmployee(employeeRepository.save(employee));

    }
    @Override
    public EmployeeDTO getEmployeeById(int id) {
        return EmployeeDTO.fromEmployee(
                employeeRepository.findById(id)
                        .orElse(new Employee())
        );
    }
    @Override
    public void deleteEmployeeById(int id) {
        employeeRepository.deleteById(id);
    }
    @Override
    public List<EmployeeDTO> getEmployeesByName (String name) {
        return employeeRepository.getEmployeesByName(name);
    }
    @Override
    public List<EmployeeFullInfo> getAllInfo() {
        return employeeRepository.findAllEmployeeFullInfo();
    }

    @Override
    public EmployeeDTO employeeWithHighestSalary() {
        return EmployeeDTO.fromEmployee(employeeRepository.employeeWithHighestSalary());
    }
    @Override
    public List<EmployeeDTO> allEmployeesByPosition(String name) {
        return employeeRepository.returnAllByPositionId(name).stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }
    @Override
    public List<Employee> getEmployeeWithPaging(int pageIndex, int unitPerPage) {
        Pageable employeeOfConcretePage = PageRequest.of(pageIndex, unitPerPage);
        Page<Employee> page = employeeRepository.findAll(employeeOfConcretePage);

        return page.stream()
                .toList();
    }
}
