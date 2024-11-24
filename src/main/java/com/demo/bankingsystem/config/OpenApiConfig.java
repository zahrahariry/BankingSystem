package com.demo.bankingsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Banking System Application API")
						.description("Spring Boot Banking System API Documentation")
						.version("1.0")
						.contact(new Contact()
								.name("zahra hariry")
								.email("z.hariry@gmail.com")));
	}
}
