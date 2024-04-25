package com.mastermind.controllers;

import com.mastermind.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {

    public static <T> ResponseEntity<Object> build(Result<T> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (result.getType()) {
            case INVALID:
                status = HttpStatus.BAD_REQUEST;
                break;
            case NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
            case SUCCESS:
                status = HttpStatus.OK;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        return new ResponseEntity<>(result.getMessages(), status);
    }

}
