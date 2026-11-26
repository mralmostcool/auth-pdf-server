package com.theneerajgupta.pdfserver.pdf.internal.repository;

import com.theneerajgupta.pdfserver.pdf.internal.entity.PdfDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PdfDocumentRepository extends JpaRepository<PdfDocumentEntity, UUID> {
}
