package com.decathlon.sample.apisecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("^\\/(authenticate|users).*"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Securing API - From basics to beyond",
                "Demo API used in combination with the same title talk presentation.",
                "1.0",
                "TERMS OF SERVICE URL",
                new Contact("Alexandre Faria","https://alexandrefaria.net","info@alexandrefaria.net"),
                "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()
        );
    }
}
