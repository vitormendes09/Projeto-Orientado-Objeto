package iff.edu.br.barbearia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BarbeariaApplicationSmokeTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }
    
    @Test
    void verificaSeTodosOsControllersForamCarregados() {
        assertThat(applicationContext.containsBean("clienteController")).isTrue();
        assertThat(applicationContext.containsBean("barbeiroController")).isTrue();
        assertThat(applicationContext.containsBean("agendamentoController")).isTrue();
        assertThat(applicationContext.containsBean("agendaController")).isTrue();
        assertThat(applicationContext.containsBean("serviceController")).isTrue();
    }
    
    @Test
    void verificaSeTodosOsServicesForamCarregados() {
        assertThat(applicationContext.containsBean("clienteServiceImpl")).isTrue();
        assertThat(applicationContext.containsBean("barbeiroServiceImpl")).isTrue();
        assertThat(applicationContext.containsBean("agendamentoServiceImpl")).isTrue();
        assertThat(applicationContext.containsBean("agendaServiceImpl")).isTrue();
        assertThat(applicationContext.containsBean("servicoServiceImpl")).isTrue();
    }
    
    @Test
    void verificaSeTodosOsRepositoriesForamCarregados() {
        assertThat(applicationContext.containsBean("jpaClienteRepository")).isTrue();
        assertThat(applicationContext.containsBean("jpaBarbeiroRepository")).isTrue();
        assertThat(applicationContext.containsBean("jpaAgendamentoRepository")).isTrue();
        assertThat(applicationContext.containsBean("jpaAgendaRepository")).isTrue();
        assertThat(applicationContext.containsBean("jpaServicoRepository")).isTrue();
    }
    
    @Test
    void verificaSeTodosOsFactoriesForamCarregados() {
        assertThat(applicationContext.containsBean("clienteFactory")).isTrue();
        assertThat(applicationContext.containsBean("barbeiroFactory")).isTrue();
        assertThat(applicationContext.containsBean("agendamentoFactory")).isTrue();
        assertThat(applicationContext.containsBean("agendaFactory")).isTrue();
        assertThat(applicationContext.containsBean("servicoFactory")).isTrue();
    }
    
    @Test
    void verificaSeTodosOsVisitorsForamCarregados() {
        assertThat(applicationContext.containsBean("clienteLogVisitor")).isTrue();
        assertThat(applicationContext.containsBean("clienteEstatisticaVisitor")).isTrue();
        assertThat(applicationContext.containsBean("agendamentoLogVisitor")).isTrue();
        assertThat(applicationContext.containsBean("agendamentoEstatisticaVisitor")).isTrue();
    }
}