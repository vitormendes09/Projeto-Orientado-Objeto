package iff.edu.br.barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "iff.edu.br.barbearia",
    "com.barbearia.backend.application",
    "com.barbearia.backend.infrastructure",
    "com.barbearia.backend.shared"
})
@EnableJpaRepositories(basePackages = "com.barbearia.backend.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.barbearia.backend.core.entities")
public class BarbeariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarbeariaApplication.class, args);
    }
}