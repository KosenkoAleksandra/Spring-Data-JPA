package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.lessons.springboot.springboot.dto.ReportDTO;
import ru.skypro.lessons.springboot.springboot.entity.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT new ru.skypro.lessons.springboot.springboot.dto.ReportDTO(e.position.name," +
            " count(e.id), max(e.salary), min(e.salary)," +
            " avg(e.salary)) FROM Employee e GROUP BY e.position.name")
    List<ReportDTO> buildReports();
}
