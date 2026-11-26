package com.theneerajgupta.pdfserver.pdf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeneratePdfRequest(
    @NotBlank(message = "Text content cannot be blank")
    @Size(max = 20000, message = "Text content must not exceed 20000 characters")
    String text,

    String title
) {}
