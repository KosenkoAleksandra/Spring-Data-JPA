package ru.skypro.lessons.springboot.springboot.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeFullInfo;
import ru.skypro.lessons.springboot.springboot.dto.ReportDTO;
import ru.skypro.lessons.springboot.springboot.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>, PagingAndSortingRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e")
    List<Employee> findAllEmployees();

    @Query(value = "SELECT * FROM employee WHERE name= :name", nativeQuery = true)
    List<Employee> getEmployeesByName(@Param("name") String name);

    @Query("SELECT new ru.skypro.lessons.springboot.springboot.dto." +
            "EmployeeFullInfo(e.name , e.salary , p.name) " +
            "FROM Employee e join fetch Position p " +
            "WHERE e.position = p")
    List<EmployeeFullInfo> findAllEmployeeFullInfo();

    @Query("SELECT new ru.skypro.lessons.springboot.springboot.dto." +
            "EmployeeFullInfo(e.name , e.salary , p.name) " +
            "FROM Employee e join fetch Position p " +
            "WHERE e.position = p AND e.id=?1")
    EmployeeFullInfo findByIdFullInfo(Integer id);

    @Query("SELECT e FROM Employee e WHERE e.salary > :salary")
    Employee employeeWithHighestSalary();

    Employee findFirstByOrderBySalaryDesc();

    @Query("SELECT e, p FROM Employee e JOIN FETCH Position p" +
        " ON e.position = p WHERE e.position.name = :position")
    List<Employee> getEmployeesByPosition(@Param("position") String position);
}
