package com.arthur.ratelimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Filter implements jakarta.servlet.Filter {
    private final LeakyBucket leakyBucket;
    private final Logger logger = LoggerFactory.getLogger(Filter.class);

    public Filter(LeakyBucket leakyBucket) {
        this.leakyBucket = leakyBucket;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Rate limiter filter started");
        if (!leakyBucket.isFull()) {
            leakyBucket.handleRequest();
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Bucket is full, dropping request");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(429);
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().println("Rate limit exceeded");
        }
    }
}
