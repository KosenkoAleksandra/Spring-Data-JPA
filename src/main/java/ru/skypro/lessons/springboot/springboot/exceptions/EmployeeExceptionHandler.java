package ru.skypro.lessons.springboot.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class EmployeeExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleIOException(IOException ioException) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception exception) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleSQLException(SQLException sqlException) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(IllegalJsonFileException.class)
    public ResponseEntity<?> badRequest() {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> internalServerError() {
        return ResponseEntity.internalServerError().build();
    }
}
