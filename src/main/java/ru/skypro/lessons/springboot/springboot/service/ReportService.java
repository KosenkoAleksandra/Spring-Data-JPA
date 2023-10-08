package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.Resource;

public interface ReportService {
    int createReport();

    String buildReport() throws JsonProcessingException;

    Resource downloadReport(int id);
}
