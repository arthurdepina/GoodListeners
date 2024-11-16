package com.goodlisteners.exception.dto;

import java.util.List;

public class ErrorResponseDto {

    private List<ErrorDto> errors;

    public ErrorResponseDto(List<ErrorDto> errors) {
        this.errors = errors;
    }

    public ErrorResponseDto() {
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDto> errors) {
        this.errors = errors;
    }
}
