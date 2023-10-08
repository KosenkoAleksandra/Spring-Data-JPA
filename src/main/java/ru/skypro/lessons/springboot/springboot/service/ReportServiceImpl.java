package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.springboot.dto.ReportDTO;
import ru.skypro.lessons.springboot.springboot.entity.Report;
import ru.skypro.lessons.springboot.springboot.exceptions.InternalServerError;
import ru.skypro.lessons.springboot.springboot.exceptions.ReportNotFoundException;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;


    @Override
    public int createReport() {
        logger.debug("createReport method was invoked");
        try {
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerError();
        }
    }

    public String buildReport() throws JsonProcessingException {
        logger.debug("buildReport method was invoked");
        List<ReportDTO> reports = reportRepository.buildReports();
        return objectMapper.writeValueAsString(reports);
    }

    @Override
    public Resource downloadReport(int id) {
        logger.debug("downloadReport method was invoked with parameter: {}", id);
        return new ByteArrayResource(
                reportRepository.findById(id)
                        .orElseThrow(ReportNotFoundException::new)
                        .getReport()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}
