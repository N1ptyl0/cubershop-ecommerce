package com.cubershop;

import com.cubershop.converter.StringToEnumConverter;
import com.cubershop.validation.CubeValidation;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverter());
    }

    @Override
    public Validator getValidator() {
        return new CubeValidation();
    }
}
