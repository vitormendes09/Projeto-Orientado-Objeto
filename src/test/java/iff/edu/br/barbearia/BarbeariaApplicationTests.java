package iff.edu.br.barbearia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BarbeariaApplicationTests {

	@Test
	void contextLoads() {
	}

}
