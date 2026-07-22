package com.medical.common.exception.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.medical.common.exception.custom.AlreadyOnWaitlistException;
import com.medical.common.exception.custom.AppointmentNotCompletedException;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.InvalidAppointmentStatusException;
import com.medical.common.exception.custom.OutsideOperatingHoursException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.SlotNotAvailableException;
import com.medical.common.exception.custom.UnauthorizedAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Builds a standard error response map
    private Map<String, Object> buildErrorResponse(HttpStatus status, String message, Object details) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        if (details != null) {
            errorResponse.put("details", details);
        }
        return errorResponse;
    }

    // 1. Handle 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put("resourceName", ex.getResourceName());
        details.put("fieldName", ex.getFieldName());
        details.put("fieldValue", ex.getFieldValue());
        
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), details), 
            HttpStatus.NOT_FOUND
        );
    }

    // 2. Handle 409 - Conflict (Duplicates, Slot Taken, Already on Waitlist)
    @ExceptionHandler({
        DuplicateResourceException.class, 
        SlotNotAvailableException.class, 
        AlreadyOnWaitlistException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflictExceptions(RuntimeException ex) {
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), null), 
            HttpStatus.CONFLICT
        );
    }

    // 3. Handle 400 - Bad Request (Operating Hours, Invalid Status, Not Completed)
    @ExceptionHandler({
        OutsideOperatingHoursException.class,
        InvalidAppointmentStatusException.class,
        AppointmentNotCompletedException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestExceptions(RuntimeException ex) {
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 4. Handle 403 - Forbidden (Unauthorized Access)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), null), 
            HttpStatus.FORBIDDEN
        );
    }

    // 5. Handle 400 - Validation Failures (Spring's @Valid annotation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 6. Catch-all 500 - Internal Server Error (For any unhandled exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        // In production, log the stack trace here: log.error("Unhandled exception", ex);
        return new ResponseEntity<>(
            buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.", null), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}