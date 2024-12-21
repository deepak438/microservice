package com.easybytes.loans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoanAlreadyExistApplication extends RuntimeException{
    public LoanAlreadyExistApplication(String message) {
        super(message);
    }
}
