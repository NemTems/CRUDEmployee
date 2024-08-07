package com.example.CATSEmployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CatsEmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsEmployeeApplication.class, args);
	}

}
