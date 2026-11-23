package com.theneerajgupta.pdfserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public API", description = "Endpoints for server health and public information")
public class HealthCheckController {

    @GetMapping("/ping")
    @Operation(summary = "Ping endpoint", description = "Returns service health and status")
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "auth-pdf-server backend is alive",
            "timestamp", System.currentTimeMillis()
        ));
    }
}
