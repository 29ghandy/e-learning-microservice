package org.example.notificationservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static void clear() {
        tokenHolder.remove();
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenHolder.get();
        if (token != null && !token.isEmpty()) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
