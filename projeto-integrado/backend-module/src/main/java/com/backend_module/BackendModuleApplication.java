package com.backend_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.backend_module",
		"com.example.ejb"
})
public class BackendModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendModuleApplication.class, args);
	}

}
