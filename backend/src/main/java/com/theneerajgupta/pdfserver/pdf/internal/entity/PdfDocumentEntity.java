package com.theneerajgupta.pdfserver.pdf.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pdf_documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfDocumentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    @Column(name = "bucket_name", nullable = false)
    private String bucketName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "page_format", nullable = false)
    private String pageFormat;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
