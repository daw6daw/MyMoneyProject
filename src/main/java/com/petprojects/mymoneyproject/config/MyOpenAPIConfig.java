package com.petprojects.mymoneyproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyOpenAPIConfig {

    @Bean
    public OpenAPI mySwaggerUI () {
        return new OpenAPI()
                .info(new Info()
                        .title("Личная бухгалтерия")
                        .description("Сервис для учёта личных финансовых операций")
                        .version("1")
                        .contact(new Contact().name("Перелетов Роман Алексеевич")));
    }
}
