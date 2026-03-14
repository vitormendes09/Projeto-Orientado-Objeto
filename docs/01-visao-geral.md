## Lógica de Serviços

### Visão Geral

O módulo de serviços é o coração do sistema de agendamento, pois define **o que** o barbeiro oferece e **como** esses serviços serão utilizados nos agendamentos. Cada serviço cadastrado impacta diretamente na disponibilidade da agenda, no cálculo de valores e na experiência do cliente.

### Regras de Negócio (RN) para Serviços

#### RN05 – Gestão de Serviços
- **RN05.1 – Responsabilidade de Cadastro**: O barbeiro é o único responsável pelo cadastro, alteração e exclusão de serviços. Clientes apenas visualizam os serviços disponíveis.
- **RN05.2 – Composição do Serviço**: Cada serviço possui obrigatoriamente: nome, preço e tempo de duração (em minutos). Descrição é opcional.
- **RN05.3 – Nome Único**: Não podem existir dois serviços com o mesmo nome, evitando duplicidade e confusão no momento do agendamento.

#### RN06 – Disponibilidade e Visibilidade
- **RN06.1 – Serviços Inativos**: Serviços podem ser desativados (soft delete) sem serem removidos do banco de dados. Isso preserva o histórico de agendamentos antigos.
- **RN06.2 – Visibilidade para Clientes**: Apenas serviços com status `ativo = true` são exibidos para clientes na hora do agendamento.
- **RN06.3 – Reativação**: Serviços desativados podem ser reativados a qualquer momento, voltando a aparecer para clientes.

#### RN07 – Impacto nos Agendamentos
- **RN07.1 – Cálculo de Tempo**: O tempo total de um agendamento é a soma da duração de todos os serviços selecionados.
- **RN07.2 – Cálculo de Valor**: O valor total é a soma dos preços individuais dos serviços.
- **RN07.3 – Congelamento de Preço**: Alterações de preço em serviços **não afetam** agendamentos já confirmados. Apenas novos agendamentos utilizam o preço atualizado.
- **RN07.4 – Bloqueio de Agenda**: Durante o período de um serviço, a agenda do barbeiro fica completamente bloqueada, impossibilitando novos agendamentos na mesma janela de tempo.

#### RN08 – Popularidade e Estatísticas
- **RN08.1 – Contador de Agendamentos**: Cada serviço mantém um contador de quantas vezes foi agendado, permitindo identificar os serviços mais populares.
- **RN08.2 – Ordenação por Popularidade**: Clientes podem visualizar serviços ordenados pelos mais agendados, auxiliando na escolha.
