package com.example.agimob_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AgimobV1Application {

	public static void main(String[] args) {
		SpringApplication.run(AgimobV1Application.class, args);
	}

}
