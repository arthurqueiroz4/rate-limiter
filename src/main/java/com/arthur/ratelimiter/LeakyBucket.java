package com.arthur.ratelimiter;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LeakyBucket {
    final Logger logger = LoggerFactory.getLogger(LeakyBucket.class);
    final Queue<Thread> queue = new ConcurrentLinkedQueue<>();
    private final int BUCKET_SIZE;
    private final int LEAK_RATE_PER_SECOND;

    public LeakyBucket(int bucketSize, int leakRatePerSecond) {
        BUCKET_SIZE = bucketSize;
        LEAK_RATE_PER_SECOND = leakRatePerSecond;
    }

    public Boolean isFull() {
        return queue.size() >= BUCKET_SIZE;
    }

    public void handleRequest() {
        synchronized (Thread.currentThread()) {
            try {
                logger.info("Virtual thread {} created and waiting", Thread.currentThread().getName());
                put(Thread.currentThread());
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                logger.error("Virtual thread interrupted", e);
            }
        }
    }

    public void put(Thread thread) {
        logger.info("Put {} (is it virtual? {}) into {}", thread.getName(), thread.isVirtual(), queue);
        queue.add(thread);
    }

    public void leak() throws InterruptedException {
        for (int i = 0; i < LEAK_RATE_PER_SECOND; i++) {
            logger.info("Queue size is {}", queue.size());
            Thread thread = queue.poll();
            if (thread != null) {
                synchronized (thread) {
                    logger.info("Resuming virtual thread {} with state: {}", thread.getName(), thread.getState());
                    thread.notify();
                }
            }
        }
    }

    @PostConstruct
    public void startRoutine() {
        logger.info("Starting Leaky Routine with throughput equal to {} per second", LEAK_RATE_PER_SECOND);
        Thread.startVirtualThread(
                () -> {
                    while (true) {
                        try {
                            leak();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Leaky routine interrupted", e);
                        }
                    }
                }
        );
    }
}
