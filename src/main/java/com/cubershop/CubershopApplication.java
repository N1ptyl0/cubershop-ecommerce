package com.cubershop;

import com.cubershop.context.converter.DoubleToPriceConverter;
import com.cubershop.context.converter.PriceFormatter;
import com.cubershop.context.converter.StringToEnumConverter;
import com.cubershop.context.validation.CubeValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class CubershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubershopApplication.class, args);
	}
}