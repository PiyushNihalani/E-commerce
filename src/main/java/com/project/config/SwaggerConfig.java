package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * 
 * Author: Kody Technolab Ltd. <br/>
 * Date : 09-May-2024
 */
@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("Kody Application").description("Common Project Backend API").version(
				"1.0"));
	}
}