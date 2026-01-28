package com.example.apigateway.ratelimit;

import lombok.Value;

@Value
public class RateLimitResult {
   boolean allowed;
   int remaining;
   long resetAfterSeconds;
}
