package com.theneerajgupta.pdfserver.pdf;

import com.theneerajgupta.pdfserver.config.jwt.JwtAuthenticationFilter;
import com.theneerajgupta.pdfserver.config.jwt.JwtService;
import com.theneerajgupta.pdfserver.pdf.dto.PdfDocumentResponse;
import com.theneerajgupta.pdfserver.pdf.internal.service.PdfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PdfController.class)
@AutoConfigureMockMvc(addFilters = false)
class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PdfService pdfService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void listPdfs_returnsPaginatedList() throws Exception {
        UUID docId = UUID.randomUUID();
        PdfDocumentResponse doc = new PdfDocumentResponse(
                docId,
                "Test Doc",
                "documents/" + docId + ".pdf",
                "pdf-documents",
                1024L,
                "application/pdf",
                "A4",
                "http://localhost:7003/presigned",
                Instant.now()
        );

        when(pdfService.getPdfDocuments(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(doc), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/pdf")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(docId.toString()))
                .andExpect(jsonPath("$.content[0].title").value("Test Doc"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
