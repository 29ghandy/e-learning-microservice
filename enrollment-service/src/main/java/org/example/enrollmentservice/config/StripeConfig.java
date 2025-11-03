package org.example.enrollmentservice.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.key}")
    private String apiKey;

    StripeConfig() {
        Stripe.apiKey = apiKey;

    }
}