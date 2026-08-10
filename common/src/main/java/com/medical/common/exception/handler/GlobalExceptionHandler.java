package com.medical.common.exception.handler;

import com.medical.common.dto.response.ErrorResponse;
import com.medical.common.dto.response.ValidationErrorResponse;
import com.medical.common.exception.custom.AlreadyOnWaitlistException;
import com.medical.common.exception.custom.AppointmentNotCompletedException;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.InvalidAppointmentStatusException;
import com.medical.common.exception.custom.OutsideOperatingHoursException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.SlotNotAvailableException;
import com.medical.common.exception.custom.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 Not Found 
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    //409 Conflict 
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request);
    }

    // 409 Conflict
    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleSlotNotAvailable(
            SlotNotAvailableException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Slot Not Available",
                ex.getMessage(), request);
    }

    //400 Bad Request 
    @ExceptionHandler(OutsideOperatingHoursException.class)
    public ResponseEntity<ErrorResponse> handleOutsideOperatingHours(
            OutsideOperatingHoursException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Outside Operating Hours",
                ex.getMessage(), request);
    }

    // 400 Bad Request 
    @ExceptionHandler(AppointmentNotCompletedException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotCompleted(
            AppointmentNotCompletedException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Appointment Not Completed",
                ex.getMessage(), request);
    }

    // 409 Conflict 
    @ExceptionHandler(AlreadyOnWaitlistException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyOnWaitlist(
            AlreadyOnWaitlistException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Already On Waitlist",
                ex.getMessage(), request);
    }

    //400 Bad Request
    @ExceptionHandler(InvalidAppointmentStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatus(
            InvalidAppointmentStatusException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Invalid Appointment Status",
                ex.getMessage(), request);
    }

    //403 Forbidden
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
            UnauthorizedAccessException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "Forbidden",
                ex.getMessage(), request);
    }

    //401 Unauthorized
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized",
                ex.getMessage(), request);
    }

    //400 Bad Request — Validation Errors 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "One or more fields have invalid values",
                request.getRequestURI(),
                validationErrors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request",
                ex.getMessage(), request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); 
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred. Please try again later.", request);
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error,
                                                  String message,
                                                  HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                status.value(), error, message, request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }
}