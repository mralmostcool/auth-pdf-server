package com.theneerajgupta.pdfserver.pdf.internal.service;

import com.theneerajgupta.pdfserver.pdf.dto.PdfStatusResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
class PdfServiceImpl implements PdfService {

    @Override
    public PdfStatusResponse getStatus() {
        return new PdfStatusResponse(
            "PDF modulith service active",
            "UP",
            Instant.now()
        );
    }
}
