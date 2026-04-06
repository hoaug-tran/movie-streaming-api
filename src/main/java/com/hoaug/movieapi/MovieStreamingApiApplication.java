package com.hoaug.movieapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieStreamingApiApplication {

	public static void main(String[] args) {
		try {
			String envPath = System.getProperty("user.dir");
			Dotenv dotenv = Dotenv.configure()
					.directory(envPath)
					.load();

			dotenv.entries()
					.forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			System.err.println("[CẢNH BÁO]: Không thẻe load file .env: " + e.getMessage());
		}

		SpringApplication.run(MovieStreamingApiApplication.class, args);
	}

}
