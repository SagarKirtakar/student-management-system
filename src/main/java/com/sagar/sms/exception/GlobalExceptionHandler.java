package com.sagar.sms.exception;

import com.sagar.sms.entity.ApiErrors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Media is not supported");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Request body is not readable");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Request method not supported");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Path variable is missing");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Request param is missing");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Mismatch of type");
        ApiErrors apiErrors = new ApiErrors(msg, details, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(apiErrors);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    protected ResponseEntity<Object> handleIdNotFoundException(StudentNotFoundException ex) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Id not available");
        ApiErrors apiErrors = new ApiErrors(msg, details, HttpStatus.NOT_FOUND, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrors);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherException(Exception ex) {
        String msg = ex.getMessage();
        List<String> details = new ArrayList<>();
        details.add("Other exception");
        details.add(ex.getMessage());
        ApiErrors apiErrors = new ApiErrors(msg, details, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> details.add(error.getDefaultMessage()));

        ApiErrors apiErrors = new ApiErrors(
                "Validation is Failed",
                details,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(apiErrors);
    }


}
