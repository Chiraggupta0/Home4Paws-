package com.home4paws.home4paws.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Assigns every incoming request a short correlation id (requestId) and stores it
 * in the SLF4J MDC, so all log lines produced while handling that request carry the
 * same [requestId] tag. Runs before every other filter (JwtFilter, security, ...).
 *
 * Logs one line when the request arrives and one when it completes (with status
 * and duration), then always clears the MDC so ids never leak across pooled threads.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Reuse an upstream id (e.g. from nginx) if present, otherwise mint one.
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString().substring(0, 8);
        }

        MDC.put(MDC_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        long start = System.currentTimeMillis();

        log.info("--> {} {}{}", method, uri, query != null ? "?" + query : "");

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("<-- {} {} {} ({}ms)", method, uri, response.getStatus(), duration);
            MDC.remove(MDC_KEY);
        }
    }
}
