package ru.skypro.lessons.springboot.springboot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<?> handleIOException(IOException ioException) {
        logger.error(ioException.getMessage(), ioException);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleSQLException(SQLException sqlException) {
        logger.error(sqlException.getMessage(), sqlException);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<?> notFound(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(IllegalJsonFileException.class)
    public ResponseEntity<?> badRequest(IllegalJsonFileException e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> internalServerError(InternalServerError e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }
}
