package com.theneerajgupta.pdfserver.pdf.internal.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Component
public class OpenPdfGenerator {

    public byte[] generateA4Document(String title, String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph titlePara = new Paragraph(title != null && !title.isBlank() ? title : "A4 Document", titleFont);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);

            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            Paragraph bodyPara = new Paragraph(text != null ? text : "", bodyFont);
            bodyPara.setLeading(16f);
            document.add(bodyPara);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate A4 PDF document: " + e.getMessage(), e);
        }
    }
}
