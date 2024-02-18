package com.bank.bank_account.exceptions.advice;

import com.bank.bank_account.exceptions.BankAccountHttpException;
import com.bank.bank_account.web.AccountController;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice(assignableTypes = {AccountController.class})
public class ControllerAdviceException {

    @ExceptionHandler(BankAccountHttpException.class)
    public ResponseEntity<BankAccountErrorResponse> handleBankAccountExceptionHttpException(BankAccountHttpException e) {

        var errorResponse = BankAccountErrorResponse.builder().message(e.getMessage()).build();
        for (var entrySet : e.getErrors().entrySet()) {
            errorResponse.addError(Error.builder()
                    .key(entrySet.getKey())
                    .value(entrySet.getValue())
                    .build());
        }

        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BankAccountErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        var errorResponse = BankAccountErrorResponse.builder().message("Argument type mismatch").build();
        errorResponse.addError(Error.builder().key(e.getName()).value("Type mismatch").build());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BankAccountErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        var errorResponse = BankAccountErrorResponse.builder().message("Missing query parameter").build();
        errorResponse.addError(Error.builder().key(e.getParameterName()).value("Must be present").build());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BankAccountErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errorResponse = BankAccountErrorResponse.builder().message("Validation error").build();
        for (var error : e.getBindingResult().getFieldErrors()) {
            errorResponse.addError(Error.builder().key(error.getField()).value(error.getDefaultMessage()).build());
        }
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BankAccountErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        var errorResponse = BankAccountErrorResponse.builder().message("Missing query parameter").build();
        for (var violation : e.getConstraintViolations()) {
            errorResponse.addError(Error.builder().key(violation.getPropertyPath().toString()).value(violation.getMessage()).build());
        }
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BankAccountErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        var errorResponse = BankAccountErrorResponse.builder().message("Invalid json for this request").build();
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class BankAccountErrorResponse {
        private String message;
        @Builder.Default
        private List<Error> errors = new ArrayList<>();

        public void addError(Error error) {
            errors.add(error);
        }
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Error {
        private String key;
        private String value;
    }
}
