package com.cubershop;

import com.cubershop.converter.*;
import com.cubershop.validation.CubeValidation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new PriceFormatter());
        registry.addConverter(new DoubleToPriceConverter());
        registry.addConverterFactory(new StringToEnumConverter());
    }

    @Override
    public Validator getValidator() {
        return new CubeValidation();
    }

    @Bean
    @Profile("prod")
    public HikariDataSource prodHikariDataSource() {
        HikariConfig config = new HikariConfig() {
            {
                setDriverClassName("org.postgresql.Driver");
                setJdbcUrl(System.getenv("URL"));
                setUsername(System.getenv("SPRING_DATASOURCE_USERNAME"));
                setPassword(System.getenv("SPRING_DATASOURCE_PASSWORD"));
            }
        };

        return new HikariDataSource(config);
    }
}
