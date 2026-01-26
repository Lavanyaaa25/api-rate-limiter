package com.example.apigateway.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RateLimitRule rule;
    private final ConcurrentHashMap<String, RateLimitCounter> counters =
            new ConcurrentHashMap<>();

    public synchronized boolean isAllowed(String clientId) {
        Instant now = Instant.now();

        RateLimitCounter counter = counters.computeIfAbsent(clientId, key -> {
            RateLimitCounter c = new RateLimitCounter();
            c.count = 0;
            c.windowStart = Instant.now();
            return c;
        });

        if (now.isAfter(counter.windowStart.plus(rule.getWindow()))) {
            counter.count = 0;
            counter.windowStart = now;
        }

        counter.count++;
        return counter.count <= rule.getMaxRequests();
    }
}
