package com.arthur.ratelimiter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LeakyBucketAlgorithm<T> implements RateLimiter {
    private final Integer BUCKET_SIZE;
    private final Queue<T> queue = new ConcurrentLinkedQueue<>();

    public LeakyBucketAlgorithm(Integer bucketSize) {
        BUCKET_SIZE = bucketSize;
    }

    @Override
    public Boolean pass() {
        // TODO: Implement this
        return false;
    }
}
