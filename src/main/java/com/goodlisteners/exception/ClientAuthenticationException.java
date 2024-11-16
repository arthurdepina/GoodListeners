package com.goodlisteners.exception;

import org.springframework.http.HttpStatus;

public class ClientAuthenticationException extends RuntimeException {

    private HttpStatus httpStatus;
    private String code;

    public ClientAuthenticationException(String message, String code, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
