package com.theneerajgupta.pdfserver.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String errorCode,
    String message,
    String path,
    String correlationId,
    Map<String, String> validationErrors
) {
    public static ApiErrorResponse of(int status,
                                      String error,
                                      String errorCode,
                                      String message,
                                      String path,
                                      String correlationId) {
        return new ApiErrorResponse(Instant.now(), status, error, errorCode, message, path, correlationId, null);
    }

    public static ApiErrorResponse of(int status,
                                      String error,
                                      String errorCode,
                                      String message,
                                      String path,
                                      String correlationId,
                                      Map<String, String> validationErrors) {
        return new ApiErrorResponse(Instant.now(), status, error, errorCode, message, path, correlationId, validationErrors);
    }
}
