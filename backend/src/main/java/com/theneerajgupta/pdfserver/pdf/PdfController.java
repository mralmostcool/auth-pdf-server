package com.theneerajgupta.pdfserver.pdf;

import com.theneerajgupta.pdfserver.pdf.dto.PdfStatusResponse;
import com.theneerajgupta.pdfserver.pdf.internal.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
@Tag(name = "PDF API", description = "Endpoints for PDF operations")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/status")
    @Operation(summary = "PDF module status", description = "Returns status of the PDF module")
    public ResponseEntity<PdfStatusResponse> getStatus() {
        return ResponseEntity.ok(pdfService.getStatus());
    }
}
