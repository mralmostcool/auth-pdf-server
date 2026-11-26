package com.theneerajgupta.pdfserver.pdf;

import com.theneerajgupta.pdfserver.pdf.dto.GeneratePdfRequest;
import com.theneerajgupta.pdfserver.pdf.dto.PdfDocumentResponse;
import com.theneerajgupta.pdfserver.pdf.dto.PdfStatusResponse;
import com.theneerajgupta.pdfserver.pdf.internal.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/pdf")
@Tag(name = "PDF API", description = "Endpoints for generating and retrieving PDF documents")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping
    @Operation(summary = "List generated PDFs", description = "Returns a paginated list of generated PDF documents")
    public ResponseEntity<Page<PdfDocumentResponse>> listPdfs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(pdfService.getPdfDocuments(pageable));
    }

    @GetMapping("/status")
    @Operation(summary = "PDF module status", description = "Returns status of the PDF module and MinIO datastore")
    public ResponseEntity<PdfStatusResponse> getStatus() {
        return ResponseEntity.ok(pdfService.getStatus());
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate A4 PDF and store in MinIO", description = "Renders text into an A4 PDF document, stores binary in MinIO, saves metadata in PostgreSQL")
    public ResponseEntity<PdfDocumentResponse> generatePdf(@Valid @RequestBody GeneratePdfRequest request) {
        PdfDocumentResponse response = pdfService.createAndStorePdf(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Download generated PDF", description = "Streams PDF binary from MinIO storage")
    public ResponseEntity<Resource> downloadPdf(@PathVariable UUID id) {
        Resource resource = pdfService.getPdfResource(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }
}
