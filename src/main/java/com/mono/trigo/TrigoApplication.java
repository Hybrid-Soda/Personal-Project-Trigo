package com.mono.trigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TrigoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrigoApplication.class, args);
	}

}
