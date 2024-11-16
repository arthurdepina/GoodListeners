package com.goodlisteners.exception;

import com.goodlisteners.exception.dto.ErrorDto;
import com.goodlisteners.exception.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
//        var errors = List.of(new ErrorDto("An error has occurred", "000"));
//        var responseBody = new ErrorResponseDto(errors);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
//    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex) {
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getMessage(), ex.getCode())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }

    @ExceptionHandler(ClientAuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleClientAuthenticationException(ClientAuthenticationException ex) {
        var responseBody = new ErrorResponseDto(List.of(new ErrorDto(ex.getMessage(), ex.getCode())));
        return ResponseEntity.status(ex.getHttpStatus().value()).body(responseBody);
    }
}
