package com.simonini.adidas.publicservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class PublicserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicserviceApplication.class, args);
	}

}
