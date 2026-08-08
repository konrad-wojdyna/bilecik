package io.github.konradwojdyna.bilecik;

import org.springframework.boot.SpringApplication;

public class TestBilecikApplication {

	public static void main(String[] args) {
		SpringApplication.from(BilecikApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
