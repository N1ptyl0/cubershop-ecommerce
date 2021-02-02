package com.cubershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class CubershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubershopApplication.class, args);
	}
}