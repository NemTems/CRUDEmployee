package com.example.CATSEmployee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = APIRequestException.class)
    public ResponseEntity<APIException> handleAPIRequestException(APIRequestException e) {
        APIException apiRequestException = APIException.builder()
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .dateTime(ZonedDateTime.now())
                .throwable(e)
                .build();
        return new ResponseEntity<>(apiRequestException, apiRequestException.getHttpStatus());
    }
}
