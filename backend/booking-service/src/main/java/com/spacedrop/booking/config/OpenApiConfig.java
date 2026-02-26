package com.spacedrop.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpaceDrop Booking Service API")
                        .description("Parking spot reservations and booking lifecycle management")
                        .version("0.0.1"));
    }
}
