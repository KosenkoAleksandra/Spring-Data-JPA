package ru.skypro.lessons.springboot.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;
import ru.skypro.lessons.springboot.springboot.entity.Position;
import ru.skypro.lessons.springboot.springboot.entity.Report;
import ru.skypro.lessons.springboot.springboot.exceptions.InternalServerError;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;
import ru.skypro.lessons.springboot.springboot.service.EmployeeServiceImpl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO.fromEmployee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private EmployeeDTO employeeDTO;
    @Mock
    private ReportRepository reportRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private final Faker faker = new Faker();
    Employee employee = generateEmployee(100, 1000);
    EmployeeDTO employeeDTO1 = fromEmployee(employee);



    @Test
    public void getAllEmployees_CorrectListOfEmployees() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 1000))
                .limit(5)
                .collect(Collectors.toList());

        List<EmployeeDTO> expected = employees.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());

        when(employeeRepository.findAllEmployees()).thenReturn(employees);

        List<EmployeeDTO> actual = employeeService.getAllEmployees();

        assertEquals(expected, actual);

        verify(employeeRepository, times(1)).findAllEmployees();

    }
    private Employee generateEmployee(int id, Integer positionId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setSalary(faker.random().nextInt(12500, 250000));
        employee.setName(faker.name().fullName());
        employee.setPosition(positionId != null ? generatePosition(positionId) : null);
        return employee;
    }
    private Position generatePosition(int id) {
        Position position = new Position();
        position.setId(id);
        position.setName(faker.company().profession());
        return position;
    }
    private EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSalary(employee.getSalary())
                .setPosition(employee.getPosition());
    }

    @Test
    public void addEmployee_givenEmployee_whenAdd() {

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        assertEquals(employeeDTO1, employeeService.addEmployee(employee));

        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    public void addBatchEmployees_CorrectAdditionOfListOfEmployees() {
        EmployeeDTO result = fromEmployee(generateEmployee(1, 1000));

        employeeService.addBatchEmployees(List.of(result));

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(captor.capture());

        Employee actual = captor.getValue();

        org.assertj.core.api.Assertions.assertThat(actual).isNotNull();
        org.assertj.core.api.Assertions.assertThat(actual.getName()).isEqualTo(result.getName());
        org.assertj.core.api.Assertions.assertThat(actual.getSalary()).isEqualTo(result.getSalary());


    }

    @Test
    public void getEmployeeById_EnterId_GetExistingEmployee() {

        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));

        assertEquals(employeeDTO1, employeeService.getEmployeeById(anyInt()));

        verify(employeeRepository, times(1)).findById(anyInt());
    }

    @Test
    public void deleteEmployeeById_WhenEnterId_EmployeeIsDeleted() {
        doNothing().doThrow( new IllegalStateException() )
                .when(employeeRepository).deleteById(anyInt());

        employeeService.deleteEmployeeById(anyInt());

        verify(employeeRepository, times(1)).deleteById(anyInt());

    }

    @Test
    public void getEmployeesByName_WhenEnterName_GetEmployees() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 1000))
                .limit(5)
                .collect(Collectors.toList());

        List<EmployeeDTO> employeeDTOList = employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        when(employeeRepository.getEmployeesByName(any())).thenReturn(employeeDTOList);

        assertEquals(employeeDTOList, employeeService.getEmployeesByName(any()));

        verify(employeeRepository, times(1)).getEmployeesByName(any());

    }

    @Test
    public void getAllInfo_AllInfoAboutAllEmployees() {
        List<EmployeeFullInfo> employees = List.of(
                new EmployeeFullInfo("Ivan", 15000, "manager"),
                new EmployeeFullInfo("Stepan", 20000, "manager")
        );

        when(employeeRepository.findAllEmployeeFullInfo()).thenReturn(employees);

        assertEquals(employees, employeeService.getAllInfo());

        verify(employeeRepository, times(1)).findAllEmployeeFullInfo();

    }

    @Test
    public void employeeWithHighestSalary_RequestIsMade_ReturnEmployeeWithHighestSalary() {

        when(employeeRepository.employeeWithHighestSalary()).thenReturn(employee);

        assertEquals(employeeDTO1, employeeService.employeeWithHighestSalary());

        verify(employeeRepository, times(1)).employeeWithHighestSalary();


    }

    @Test
    public void allEmployeesByPosition_WhenEnterPosition_GetAllEmployeesOfThisPosition() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 1000))
                .limit(5)
                .collect(Collectors.toList());

        List<EmployeeDTO> expected = employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        when(employeeRepository.returnAllByPositionId(any())).thenReturn(employees);

        List<EmployeeDTO> actual = employeeService.allEmployeesByPosition(any());

        assertEquals(expected, actual);

        verify(employeeRepository, times(1)).returnAllByPositionId(any());


    }

    @Test
    public void getEmployeeWithPaging() {

    }

    @Test
    public void upload() {

    }

    @Test
    public void createReport_shouldThrowExceptionWhenRepositoryThrowsExceptions() {
//        Report report = new Report(1, "report", null);
//
//        when(reportRepository.save(any())).thenReturn(report);
//
//        assertEquals(report, employeeService.createReport());
//
//        verify(reportRepository, times(1)).save(report);

    }

    @Test
    public void buildReport () {

    }

    @Test
    public void downloadReport () {

    }

}
