package com.enterprise.product.product_service.domain.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value("${api.doc.title}")
    private static String title;
    @Value("${api.doc.version}")
    private static String version;
    @Value("${api.doc.description}")
    private static String description;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(title).version(version).description(description));
    }
}
