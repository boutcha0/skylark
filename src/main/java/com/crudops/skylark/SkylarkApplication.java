package com.crudops.skylark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.crudops.skylark")

public class SkylarkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkylarkApplication.class, args);
	}

}
