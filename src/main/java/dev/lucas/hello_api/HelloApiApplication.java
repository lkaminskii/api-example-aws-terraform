package dev.lucas.hello_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "dev.lucas.hello_api")
public class HelloApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloApiApplication.class, args);
	}

}

