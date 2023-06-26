package com.devsu.bankapi.config;

import org.modelmapper.ModelMapper;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public AppConfig() {};

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public GroupedOpenApi myApiGroup() {
        return GroupedOpenApi.builder()
                .group("devsu-group")
                .pathsToMatch("/api/**") // Rutas que deseas incluir
                .build();
    }


}
