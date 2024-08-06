package com.example.infrastructure.controller.error;

public record ApiError(int status, String message) {
}
