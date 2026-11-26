package com.theneerajgupta.pdfserver.pdf.internal.service;

import com.theneerajgupta.pdfserver.config.exception.ResourceNotFoundException;
import com.theneerajgupta.pdfserver.pdf.dto.GeneratePdfRequest;
import com.theneerajgupta.pdfserver.pdf.dto.PdfDocumentResponse;
import com.theneerajgupta.pdfserver.pdf.dto.PdfStatusResponse;
import com.theneerajgupta.pdfserver.pdf.internal.entity.PdfDocumentEntity;
import com.theneerajgupta.pdfserver.pdf.internal.repository.PdfDocumentRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Service
class PdfServiceImpl implements PdfService {

        private final OpenPdfGenerator pdfGenerator;
        private final MinioStorageService storageService;
        private final PdfDocumentRepository repository;

        public PdfServiceImpl(OpenPdfGenerator pdfGenerator,
                        MinioStorageService storageService,
                        PdfDocumentRepository repository) {
                this.pdfGenerator = pdfGenerator;
                this.storageService = storageService;
                this.repository = repository;
        }

        @Override
        public PdfStatusResponse getStatus() {
                return new PdfStatusResponse(
                                "PDF modulith service active with MinIO datastore",
                                "UP",
                                Instant.now());
        }

        @Override
        @Transactional
        public PdfDocumentResponse createAndStorePdf(GeneratePdfRequest request) {
                UUID documentId = UUID.randomUUID();
                String title = request.title() != null && !request.title().isBlank()
                                ? request.title()
                                : "Untitled Document";

                byte[] pdfBytes = pdfGenerator.generateA4Document(title, request.text());
                String storageKey = "documents/" + documentId + ".pdf";
                String contentType = "application/pdf";

                storageService.uploadFile(storageKey, pdfBytes, contentType);

                PdfDocumentEntity entity = PdfDocumentEntity.builder()
                                .id(documentId)
                                .title(title)
                                .storageKey(storageKey)
                                .bucketName(storageService.getBucketName())
                                .fileSize((long) pdfBytes.length)
                                .contentType(contentType)
                                .pageFormat("A4")
                                .createdAt(Instant.now())
                                .build();

                repository.save(entity);

                String downloadUrl = storageService.getPresignedDownloadUrl(storageKey, 30);

                return new PdfDocumentResponse(
                                entity.getId(),
                                entity.getTitle(),
                                entity.getStorageKey(),
                                entity.getBucketName(),
                                entity.getFileSize(),
                                entity.getContentType(),
                                entity.getPageFormat(),
                                downloadUrl,
                                entity.getCreatedAt());
        }

        @Override
        @Transactional(readOnly = true)
        public Resource getPdfResource(UUID id) {
                PdfDocumentEntity entity = repository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "PDF document not found with id: " + id));

                InputStream stream = storageService.downloadFile(entity.getStorageKey());
                return new InputStreamResource(stream);
        }

        @Override
        @Transactional(readOnly = true)
        public org.springframework.data.domain.Page<PdfDocumentResponse> getPdfDocuments(org.springframework.data.domain.Pageable pageable) {
                return repository.findAll(pageable)
                                .map(entity -> new PdfDocumentResponse(
                                                entity.getId(),
                                                entity.getTitle(),
                                                entity.getStorageKey(),
                                                entity.getBucketName(),
                                                entity.getFileSize(),
                                                entity.getContentType(),
                                                entity.getPageFormat(),
                                                storageService.getPresignedDownloadUrl(entity.getStorageKey(), 30),
                                                entity.getCreatedAt()));
        }

        @Override
        @Transactional
        public void deletePdf(UUID id) {
                PdfDocumentEntity entity = repository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "PDF document not found with id: " + id));

                storageService.deleteFile(entity.getStorageKey());
                repository.delete(entity);
        }
}
