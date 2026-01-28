package com.example.apigateway.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RateLimitRule rule;
    private final ConcurrentHashMap<String, RateLimitCounter> counters =
            new ConcurrentHashMap<>();

    public synchronized RateLimitResult check(String clientId) {
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

        int remaining = rule.getMaxRequests() - counter.count;
        boolean allowed = remaining >= 0;

        long resetSeconds =
                rule.getWindow().minusSeconds(
                        Duration.between(counter.windowStart, now).getSeconds()
                ).getSeconds();

        return new RateLimitResult(
                allowed,
                Math.max(remaining, 0),
                Math.max(resetSeconds, 0)
        );
    }
}
