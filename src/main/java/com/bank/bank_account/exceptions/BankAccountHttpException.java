package com.bank.bank_account.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankAccountHttpException extends RuntimeException {

    HttpStatus statusCode;
    Map<String, String> errors;

    private BankAccountHttpException(String message, HttpStatus statusCode, Map<String, String> errors) {
        super(message);
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public static BankAccountHttpException notFound(String message) {
        return new BankAccountHttpException(message, NOT_FOUND, new HashMap<>());
    }

    public static BankAccountHttpException badRequest(String message) {
        return new BankAccountHttpException(message, BAD_REQUEST, new HashMap<>());
    }
}
