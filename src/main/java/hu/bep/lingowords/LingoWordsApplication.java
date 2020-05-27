package hu.bep.lingowords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"hu.bep.lingowords.controller", "hu.bep.lingowords.repository"})
public class LingoWordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LingoWordsApplication.class, args);
	}
}
