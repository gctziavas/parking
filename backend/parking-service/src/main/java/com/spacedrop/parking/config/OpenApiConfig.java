package com.spacedrop.parking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parkingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpaceDrop Parking Service API")
                        .description("Parking lot CRUD, availability, and location-based search")
                        .version("0.0.1"));
    }
}
