package com.arthur.ratelimiter;

public record Bucket(
        int CURRENT,
        int SIZE,
        int LEAK_RATE
) {
}
