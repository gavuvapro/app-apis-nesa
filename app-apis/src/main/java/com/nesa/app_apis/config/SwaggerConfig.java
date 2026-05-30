package com.nesa.app_apis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Exam Starter API")
                                .version("1.0")
                                .description("NESA Practical Exam")
                                .contact(
                                        new Contact()
                                                .name("Fabrice")
                                                .email("fabricecoder009@gmail.com")
                                )
                );
    }
}