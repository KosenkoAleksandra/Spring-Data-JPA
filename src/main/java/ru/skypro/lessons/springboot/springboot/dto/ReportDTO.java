package ru.skypro.lessons.springboot.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String position;
    private long countEmployees;
    private int maxSalary;
    private int minSalary;
    private double averageSalary;
}
