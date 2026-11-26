package com.theneerajgupta.pdfserver.pdf.internal.service;

import com.theneerajgupta.pdfserver.pdf.dto.GeneratePdfRequest;
import com.theneerajgupta.pdfserver.pdf.dto.PdfDocumentResponse;
import com.theneerajgupta.pdfserver.pdf.dto.PdfStatusResponse;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface PdfService {
    PdfStatusResponse getStatus();
    PdfDocumentResponse createAndStorePdf(GeneratePdfRequest request);
    Resource getPdfResource(UUID id);
}
