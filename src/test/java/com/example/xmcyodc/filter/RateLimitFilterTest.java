package com.example.xmcyodc.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class RateLimitFilterTest {
    private static final int WINDOW_SIZE_IN_SECONDS = 60;
    private static final int MAX_REQUESTS_PER_WINDOW = 10;
    private static final String TEST_IP_ADDRESS = "127.0.0.1";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private RateLimitFilter rateLimitFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rateLimitFilter = new RateLimitFilter(WINDOW_SIZE_IN_SECONDS, MAX_REQUESTS_PER_WINDOW);
    }

    @Test
    public void testRequestWithinRateLimit() throws IOException, ServletException {
        when(request.getRemoteAddr()).thenReturn(TEST_IP_ADDRESS);

        // Send the first request - should be allowed
        rateLimitFilter.doFilter(request, response, filterChain);

        // Send the next 9 requests - should all be allowed
        for (int i = 0; i < 9; i++) {
            rateLimitFilter.doFilter(request, response, filterChain);
        }

        // Verify that all requests were allowed
        verify(filterChain, times(10)).doFilter(request, response);
    }

    @Test
    public void testRequestExceedsRateLimit() throws IOException, ServletException {
        when(request.getRemoteAddr()).thenReturn(TEST_IP_ADDRESS);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        // Send 11 requests - should block the last request
        for (int i = 0; i < 11; i++) {
            rateLimitFilter.doFilter(request, response, filterChain);
        }

        // Verify that the last request was blocked
        verify(response).setStatus(eq(HttpStatus.TOO_MANY_REQUESTS.value()));
    }

    @Test
    public void testConcurrentRequestsFromDifferentIPAddresses() throws InterruptedException, ServletException, IOException {
        int numThreads = 10;
        int numRequestsPerThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<String> ipAddresses = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            String ipAddress = "192.168.0." + i;
            ipAddresses.add(ipAddress);
            for (int j = 0; j < numRequestsPerThread; j++) {
                HttpServletRequest req = mock(HttpServletRequest.class);
                when(req.getRemoteAddr()).thenReturn(ipAddress);
                executor.submit(() -> {
                    try {
                        rateLimitFilter.doFilter(req, response, filterChain);
                    } catch (IOException | ServletException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Verify that all requests were successful 
        verify(filterChain, times(numThreads * numRequestsPerThread))
                .doFilter(any(ServletRequest.class), any(ServletResponse.class));

    }
}