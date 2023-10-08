package ru.skypro.lessons.springboot.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skypro.lessons.springboot.springboot.entity.Employee;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors (chain = true)
public class EmployeeDTO{
    private Integer id;
    private String name;
    private Integer salary;
    private PositionDTO positionDTO;

    public static EmployeeDTO fromEmployee(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSalary(employee.getSalary());

        if (employee.getPosition() != null) {
            dto.setPositionDTO(PositionDTO.fromPosition(employee.getPosition()));
        }

        return dto;
    }

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setName(this.getName());
        employee.setSalary(this.getSalary());

        if (this.getPositionDTO() != null) {
            employee.setPosition(this.getPositionDTO().toPosition());
        }

        return employee;
    }
}