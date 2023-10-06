package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.exceptions.IllegalJsonFileException;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("getAllEmployees method was invoked");
        return employeeRepository.findAllEmployees().stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO addEmployee(Employee employee) {
        logger.debug("addEmployee method was invoked with parameter: {}", employee);
        return EmployeeDTO.fromEmployee(employeeRepository.save(employee));
    }

    @Override
    public void addBatchEmployees(List<EmployeeDTO> employees) {
        logger.debug("addBatchEmployees method was invoked with parameter: {}", employees);
        employees.stream()
                .map(EmployeeDTO::toEmployee)
                .forEach(employeeRepository::save);
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        logger.debug("getEmployeeById method was invoked with parameter: {}", id);
        return EmployeeDTO.fromEmployee(
                employeeRepository.findById(id)
                        .orElse(new Employee())
        );
    }

    @Override
    public void deleteEmployeeById(int id) {
        logger.debug("deleteEmployeeById method was invoked with parameter: {}", id);
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByName(String name) {
        logger.debug("getEmployeesByName method was invoked with parameter: {}", name);
        return employeeRepository.getEmployeesByName(name).stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeFullInfo getEmployeeByIdFullInfo(Integer id) {
        logger.info("Was invoked method for getting employee with short description with id={}", id);
        return employeeRepository.findByIdFullInfo(id);
    }

    @Override
    public List<EmployeeFullInfo> getAllInfo() {
        logger.debug("getAllInfo method was invoked");
        return employeeRepository.findAllEmployeeFullInfo();
    }

    @Override
    public EmployeeDTO employeeWithHighestSalary() {
        logger.debug("employeeWithHighestSalary method was invoked");
        return EmployeeDTO.fromEmployee(employeeRepository.findFirstByOrderBySalaryDesc());
    }

    @Override
    public List<EmployeeDTO> allEmployeesByPosition(String name) {
        logger.debug("allEmployeesByPosition method was invoked with parameter: {}", name);
        return employeeRepository.getEmployeesByPosition(name).stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> getEmployeeWithPaging(int pageNum, int pageSize) {
        logger.debug("getEmployeeWithPaging method was invoked with parameters: {}, {}", pageNum, pageSize);
        Pageable employeeOfConcretePage = PageRequest.of(pageNum, pageSize);
        Page<Employee> page = employeeRepository.findAll(employeeOfConcretePage);

        return page.stream()
                .toList();
    }

    @Override
    public void upload(MultipartFile employees) {
        logger.debug("upload method was invoked with fileName: {}", employees.getOriginalFilename());
        try {
            String extension = StringUtils.getFilenameExtension(employees.getOriginalFilename());
            if (!"json".equals(extension)) {
                throw new IllegalJsonFileException();
            }
            List<EmployeeDTO> employeeDTOS = objectMapper.readValue(
                    employees.getBytes(),
                    new TypeReference<>() {
                    }
            );
            addBatchEmployees(employeeDTOS);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalJsonFileException();
        }
    }
}
