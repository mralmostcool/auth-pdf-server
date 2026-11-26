package com.theneerajgupta.pdfserver.pdf.dto;

import java.time.Instant;
import java.util.UUID;

public record PdfDocumentResponse(
    UUID id,
    String title,
    String storageKey,
    String bucketName,
    long fileSize,
    String contentType,
    String pageFormat,
    String downloadUrl,
    Instant createdAt
) {}
