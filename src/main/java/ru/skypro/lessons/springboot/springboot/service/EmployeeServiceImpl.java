package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.dto.ReportDTO;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Report;
import ru.skypro.lessons.springboot.springboot.exceptions.IllegalJsonFileException;
import ru.skypro.lessons.springboot.springboot.exceptions.InternalServerError;
import ru.skypro.lessons.springboot.springboot.exceptions.ReportNotFoundException;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final ReportRepository reportRepository;

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
    @Override
    public void upload(MultipartFile employees) {
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
            getAllEmployees();
        } catch (IOException e) {
            throw new IllegalJsonFileException();
        }
    }
    @Override
    public int createReport() {
        try {
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            throw new InternalServerError();
        }
    }
    public String buildReport() throws JsonProcessingException {
      List<ReportDTO> reports = employeeRepository.buildReports();
      return objectMapper.writeValueAsString(reports);
    }

    @Override
    public Resource downloadReport(int id) {
        return new ByteArrayResource(
                reportRepository.findById(id)
                        .orElseThrow(ReportNotFoundException::new)
                        .getReport()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}
