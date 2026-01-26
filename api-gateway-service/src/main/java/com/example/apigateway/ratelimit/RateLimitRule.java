package com.example.apigateway.ratelimit;

import lombok.Value;

import java.time.Duration;

@Value
public class RateLimitRule {
    int maxRequests;
    Duration window;
}
