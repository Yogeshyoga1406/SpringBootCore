package com.beautyhub.beautyhub_service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


/*
 * This configuration is required to handle Cross-Origin Resource Sharing (CORS)
 * Spring Boot App runs in say port 8080 but our client angular app runs in 4000
 * many browsers do not support accessing 8080 from 4000 and throw error like below
 * 'Response to preflight request doesn't pass access control check'
 *
 */

@Configuration
public class RestConfig {

//	@Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("DELETE");
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("corsConfigurationSource inside");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        // configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200", "https://uat.brainibooks.com", "https://test.brainibooks.com", "https://app.suktha.com"));

        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
