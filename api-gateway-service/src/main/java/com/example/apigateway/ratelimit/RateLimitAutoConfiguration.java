package com.example.apigateway.ratelimit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(
        prefix = "rate-limit",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RateLimitAutoConfiguration {

    @Bean
    public RateLimitRule rateLimitRule(RateLimitProperties props) {
        return new RateLimitRule(
                props.getMaxRequests(),
                Duration.ofSeconds(props.getWindowSeconds())
        );
    }

    @Bean
    public RateLimitService rateLimitService(RateLimitRule rule) {
        return new RateLimitService(rule);
    }

    @Bean
    public RateLimitFilter rateLimitFilter(
            RateLimitService service,
            RateLimitRule rule
    ) {
        return new RateLimitFilter(service, rule);
    }
}
