package com.miguel.biblioteca;

import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.repositories.IRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {

	private final IRoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		roleRepository.save(new Role("ROLE_ADMIN"));
		roleRepository.save(new Role("ROLE_MANAGER"));
		roleRepository.save(new Role("ROLE_LIBRARIAN"));
	}
}
