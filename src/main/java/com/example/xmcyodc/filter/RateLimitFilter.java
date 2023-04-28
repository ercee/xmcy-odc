package com.example.xmcyodc.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitFilter implements Filter {

    @Value("${ratelimiting.windowSizeInSeconds}")
    private int windowSizeInSeconds;

    @Value("${ratelimiting.maxRequestsPerWindow}")
    private int maxRequestsPerWindow;
    private final ConcurrentHashMap<String, Bucket> buckets;


    public RateLimitFilter() {
        buckets = new ConcurrentHashMap<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIpAddress = getClientIpAddress(httpRequest);

        Bucket tokenBucket = buckets.computeIfAbsent(clientIpAddress, key -> Bucket.builder()
                .addLimit(Bandwidth.classic(maxRequestsPerWindow, Refill.intervally(maxRequestsPerWindow, Duration.ofSeconds(windowSizeInSeconds))))
                .build());

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Too many requests from this IP address. Please try again later.");
            httpResponse.getWriter().flush();
        }
    }


    private static String getClientIpAddress(HttpServletRequest httpRequest) {
        String clientIpAddress = httpRequest.getHeader("X-Forwarded-For");
        if (clientIpAddress == null || clientIpAddress.isEmpty() || "unknown".equalsIgnoreCase(clientIpAddress)) {
            clientIpAddress = httpRequest.getHeader("Proxy-Client-IP");
        }
        if (clientIpAddress == null || clientIpAddress.isEmpty() || "unknown".equalsIgnoreCase(clientIpAddress)) {
            clientIpAddress = httpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIpAddress == null || clientIpAddress.isEmpty() || "unknown".equalsIgnoreCase(clientIpAddress)) {
            clientIpAddress = httpRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIpAddress == null || clientIpAddress.isEmpty() || "unknown".equalsIgnoreCase(clientIpAddress)) {
            clientIpAddress = httpRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIpAddress == null || clientIpAddress.isEmpty() || "unknown".equalsIgnoreCase(clientIpAddress)) {
            clientIpAddress = httpRequest.getRemoteAddr();
        }
        return clientIpAddress;
    }

}



