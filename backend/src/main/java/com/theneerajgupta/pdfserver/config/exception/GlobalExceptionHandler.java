package com.theneerajgupta.pdfserver.config.exception;

import com.theneerajgupta.pdfserver.config.logger.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String getCorrelationId() {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_KEY);
        return correlationId != null ? correlationId : "N/A";
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();
        log.warn("Application exception: [{}] {} on {}", ex.getErrorCode(), ex.getMessage(), request.getRequestURI());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            ex.getErrorCode(),
            ex.getMessage(),
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validation failed for {}: {}", request.getRequestURI(), errors);

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "VALIDATION_FAILED",
            "Input validation failed",
            request.getRequestURI(),
            getCorrelationId(),
            errors
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        log.warn("Constraint violation on {}: {}", request.getRequestURI(), errors);

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "CONSTRAINT_VIOLATION",
            "Constraint violation occurred",
            request.getRequestURI(),
            getCorrelationId(),
            errors
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("Malformed HTTP request body on {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "MALFORMED_REQUEST_BODY",
            "Malformed JSON or unreadable request body",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("Missing parameter '{}' on {}", ex.getParameterName(), request.getRequestURI());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "MISSING_PARAMETER",
            "Required parameter '" + ex.getParameterName() + "' is missing",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        log.warn("Method {} not allowed on {}", ex.getMethod(), request.getRequestURI());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "METHOD_NOT_ALLOWED",
            "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        log.warn("Unsupported media type on {}: {}", request.getRequestURI(), ex.getContentType());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "UNSUPPORTED_MEDIA_TYPE",
            "Content-Type '" + ex.getContentType() + "' is not supported",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "RESOURCE_NOT_FOUND",
            "Endpoint not found: " + request.getRequestURI(),
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.warn("Authentication failure on {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "UNAUTHORIZED",
            ex.getMessage(),
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        log.warn("Access denied on {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "FORBIDDEN",
            "Access is denied",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unhandled internal server error on {}", request.getRequestURI(), ex);

        ApiErrorResponse body = ApiErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            "INTERNAL_SERVER_ERROR",
            "An unexpected internal error occurred",
            request.getRequestURI(),
            getCorrelationId()
        );
        return new ResponseEntity<>(body, status);
    }
}
