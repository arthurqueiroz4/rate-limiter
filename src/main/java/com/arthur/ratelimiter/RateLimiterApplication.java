package com.arthur.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Configuration
@RestController
public class RateLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimiterApplication.class, args);
    }

    @GetMapping
    public ResponseEntity<String> hello() throws InterruptedException {
        return ResponseEntity.ok("Hello World");
    }

    @Bean
    public LeakyBucket leakyBucketAlgorithm(
            @Value("${leaky.bucket-size}") Integer bucketSize,
            @Value("${leaky.leak-rate-per-second}") Integer leakRatePerSecond
    ) {
        return new LeakyBucket(bucketSize, leakRatePerSecond);
    }

}
