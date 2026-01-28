package com.example.apigateway.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitRule rateLimitRule;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientId = request.getRemoteAddr();

        RateLimitResult result = rateLimitService.check(clientId);

        // ---- Rate limit headers ----
        response.setHeader(
                "X-RateLimit-Limit",
                String.valueOf(rateLimitRule.getMaxRequests())
        );
        response.setHeader(
                "X-RateLimit-Remaining",
                String.valueOf(result.getRemaining())
        );
        response.setHeader(
                "X-RateLimit-Reset",
                String.valueOf(result.getResetAfterSeconds())
        );

        if (!result.isAllowed()) {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
