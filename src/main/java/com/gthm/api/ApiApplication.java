package com.gthm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}


//	public static void main(String[] args) throws JsonProcessingException {
//
//		Pokemon pokemon = Instancio.create(Pokemon.class);
//		System.out.println(new ObjectMapper().writeValueAsString(pokemon));
//
//
//	}

}
