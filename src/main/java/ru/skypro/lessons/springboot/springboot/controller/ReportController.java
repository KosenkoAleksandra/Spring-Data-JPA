package ru.skypro.lessons.springboot.springboot.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;
@AllArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {
    private final EmployeeService employeeService;
    @PostMapping
    public int createReport() {
        return employeeService.createReport();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable int id) {
        Resource resource = employeeService.downloadReport(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.json\"")
                .body(resource);
    }
}
