package com.theneerajgupta.pdfserver.config.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    private final RequestMappingHandlerMapping handlerMapping;

    public RequestResponseLoggingFilter(
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/scalar") ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, MAX_PAYLOAD_LENGTH);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logSummary(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logSummary(ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            long duration) {
        String controllerInfo = resolveControllerInfo(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        int status = response.getStatus();

        String requestHeaders = formatHeaders(request);
        String requestBody = getPayload(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getPayload(response.getContentAsByteArray(), response.getCharacterEncoding());

        String summary = "\n" +
                "============================== [HTTP TRANSACTION SUMMARY] ==============================\n" +
                " Controller  : " + controllerInfo + "\n" +
                " Endpoint    : " + method + " " + uri + queryString + "\n" +
                " Status      : " + status + " (" + duration + " ms)\n" +
                "---------------------------------- [PACKET RECEIVED] -----------------------------------\n" +
                " Headers     : " + requestHeaders + "\n" +
                " Body        : " + requestBody + "\n" +
                "----------------------------------- [RESPONSE SENT] ------------------------------------\n" +
                " Body        : " + responseBody + "\n" +
                "========================================================================================";

        if (status >= 500) {
            log.error(summary);
        } else if (status >= 400) {
            log.warn(summary);
        } else {
            log.info(summary);
        }
    }

    private String resolveControllerInfo(HttpServletRequest request) {
        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
            if (handlerChain != null && handlerChain.getHandler() instanceof HandlerMethod handlerMethod) {
                return handlerMethod.getBeanType().getSimpleName() + "#" + handlerMethod.getMethod().getName() + "()";
            }
        } catch (Exception ignored) {
            // fallback if handler cannot be resolved
        }
        return "Unknown / Unmapped Handler";
    }

    private String formatHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return "{}";
        }

        Map<String, String> headers = new LinkedHashMap<>();
        for (String name : Collections.list(headerNames)) {
            if ("authorization".equalsIgnoreCase(name) || "cookie".equalsIgnoreCase(name)) {
                headers.put(name, "[REDACTED]");
            } else {
                headers.put(name, request.getHeader(name));
            }
        }
        return headers.toString();
    }

    private String getPayload(byte[] buf, String characterEncoding) {
        if (buf == null || buf.length == 0) {
            return "[EMPTY]";
        }
        try {
            String charset = characterEncoding != null ? characterEncoding : StandardCharsets.UTF_8.name();
            int length = Math.min(buf.length, MAX_PAYLOAD_LENGTH);
            String payload = new String(buf, 0, length, charset).trim();
            if (payload.isEmpty()) {
                return "[EMPTY]";
            }
            if (buf.length > MAX_PAYLOAD_LENGTH) {
                payload += " ... [TRUNCATED (" + buf.length + " bytes total)]";
            }
            return payload;
        } catch (Exception e) {
            return "[BINARY / UNPARSEABLE DATA: " + buf.length + " bytes]";
        }
    }
}
