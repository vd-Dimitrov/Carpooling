package org.example.carpooling.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
        @Bean
        public GroupedOpenApi publicApi() {
            return GroupedOpenApi
                    .builder()
                    .group("public")
                    .pathsToMatch("/api/**")
                    .build();
        }

        @Bean
        public GlobalOpenApiCustomizer globalHeaderOpenApiCustomizer() {
            return openApi -> openApi
                    .getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                        Parameter authorizationHeader = new Parameter()
                                .name("Authorization")
                                .description("Enter credentials: 'username password'")
                                .required(true)
                                .in(ParameterIn.HEADER.toString());
                        operation.addParametersItem(authorizationHeader);
                    }));
        }

}
