package com.jakan.sla_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SlaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlaServiceApplication.class, args);
	}

}
