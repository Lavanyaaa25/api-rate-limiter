package com.example.apigateway.ratelimit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private int maxRequests = 5;
    private int windowSeconds = 10;
}
