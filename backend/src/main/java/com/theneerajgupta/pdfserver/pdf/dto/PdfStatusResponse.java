package com.theneerajgupta.pdfserver.pdf.dto;

import java.time.Instant;

public record PdfStatusResponse(
    String message,
    String status,
    Instant timestamp
) {}
