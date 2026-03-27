package com.example.asset_manager.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Comma-separated allowed origins.
     * Example:
     * app.cors.allowed-origins=http://localhost:4200,http://127.0.0.1:4200
     */
    @Value("${app.cors.allowed-origins:http://localhost:4200,http://127.0.0.1:4200}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        List<String> origins = List.of(allowedOrigins.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        registry.addMapping("/**")
                // More forgiving for dev than allowedOrigins()
                .allowedOriginPatterns(origins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}