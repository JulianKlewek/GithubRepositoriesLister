package com.example.infrastructure.controller.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotExistingGithubUserException.class)
    public final ResponseEntity<ApiError> handleException(Exception exception, WebRequest webRequest) {
        HttpHeaders headers = new HttpHeaders();
        if (exception instanceof NotExistingGithubUserException githubUserException) {
            return handleNotExistingGithubUser(githubUserException, webRequest, headers);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(exception, webRequest, headers, status, null);
        }
    }

    private ResponseEntity<ApiError> handleNotExistingGithubUser(NotExistingGithubUserException githubUserException,
                                                                 WebRequest webRequest, HttpHeaders headers) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError(status.value(), githubUserException.getMessage());
        return handleExceptionInternal(githubUserException, webRequest, headers, status, apiError);
    }

    private ResponseEntity<ApiError> handleExceptionInternal(Exception exception, WebRequest webRequest,
                                                             HttpHeaders headers, HttpStatus status, ApiError apiError) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            webRequest.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(apiError, headers, status);
    }

}
