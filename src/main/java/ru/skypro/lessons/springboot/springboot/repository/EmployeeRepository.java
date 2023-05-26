package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllEmployees();
    @Query(value = "SELECT * FROM employee WHERE name= :name",
            nativeQuery = true)
    List<EmployeeDTO> getEmployeesByName(@Param("name") String name);
    @Query("SELECT new ru.skypro.lessons.springboot.weblibrary.dto." +
            "EmployeeFullInfo(e.name , e.salary , p.name) " +
            "FROM Employee e join fetch Position p " +
            "WHERE e.position = p")
    List<EmployeeFullInfo> findAllEmployeeFullInfo();
    @Query("SELECT max(salary) from employee")
    Employee employeeWithHighestSalary();
    @Query(value = "SELECT * FROM position WHERE name= :name",
            nativeQuery = true)
    List<EmployeeDTO> allEmployeesPosition();



}
