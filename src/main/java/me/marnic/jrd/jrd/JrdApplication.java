package me.marnic.jrd.jrd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Uses the native Windows Api via JNA with Spring as the backend
 */
@SpringBootApplication
public class JrdApplication {

	public static void main(String[] args) {
		SpringApplication.run(JrdApplication.class, args);
	}

}
