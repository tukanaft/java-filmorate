package ru.yandex.practicum.filmorate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
