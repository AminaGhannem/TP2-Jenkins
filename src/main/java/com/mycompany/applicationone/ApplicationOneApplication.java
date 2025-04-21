package com.mycompany.applicationone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationOneApplication {

	//some changes
	public static void main(String[] args) {
		System.out.println("hello world ");
		SpringApplication.run(ApplicationOneApplication.class, args);
	}

}
