package iff.edu.br.barbearia;

import org.springframework.boot.SpringApplication;

public class TestBarbeariaApplication {

	public static void main(String[] args) {
		SpringApplication.from(BarbeariaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
