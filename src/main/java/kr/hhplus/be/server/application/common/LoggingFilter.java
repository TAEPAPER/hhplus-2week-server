package kr.hhplus.be.server.application.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        long startTime = System.currentTimeMillis();
        
        // 요청 로깅
        logRequest(requestWrapper, requestId);
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 응답 로깅
            logResponse(responseWrapper, requestId, System.currentTimeMillis() - startTime);
            responseWrapper.copyBodyToResponse();
        }
    }
    
    private void logRequest(ContentCachingRequestWrapper request, String requestId) {
        String queryString = request.getQueryString();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        log.info("[{}] >>> {} {} {}", 
                requestId,
                method,
                requestURI + (queryString != null ? "?" + queryString : ""),
                new String(request.getContentAsByteArray()));
    }
    
    private void logResponse(ContentCachingResponseWrapper response, String requestId, long duration) {
        int status = response.getStatus();
        
        log.info("[{}] <<< {} ({}ms) {}", 
                requestId,
                status,
                duration,
                new String(response.getContentAsByteArray()));
    }
}