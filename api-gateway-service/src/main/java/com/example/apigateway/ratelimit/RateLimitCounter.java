package com.example.apigateway.ratelimit;

import java.time.Instant;

public class RateLimitCounter {
    int count;
    Instant windowStart;
}
