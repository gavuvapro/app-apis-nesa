package com.nesa.app_apis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;
}

/// Gonna be used everywhere for better api response
