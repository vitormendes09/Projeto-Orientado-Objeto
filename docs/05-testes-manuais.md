# Lista de testes manuais que podem ser feitos pelo terminal

## Clientes

### Criar cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","telefone":"22998041266"}'
```

### Listar clientes
```bash
curl http://localhost:8080/api/clientes
```

### Buscar cliente por ID
```bash
curl http://localhost:8080/api/clientes/1
```

### Buscar cliente por telefone
```bash
curl http://localhost:8080/api/clientes/telefone/22998041266
```

### Atualizar cliente
```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva Santos","telefone":"22998041266"}'
```

### Deletar cliente
```bash
curl -X DELETE http://localhost:8080/api/clientes/1
```

## Barbeiros

### Criar barbeiro
```bash
curl -X POST http://localhost:8080/api/barbeiros \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Barbeiro",
    "email": "carlos@barbearia.com",
    "senha": "123456",
    "telefone": "21987654321",
    "ativo": true
  }'
```

### Login do barbeiro
```bash
curl -X POST http://localhost:8080/api/barbeiros/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "carlos@barbearia.com",
    "senha": "123456"
  }'
```

### Listar todos os barbeiros
```bash
curl http://localhost:8080/api/barbeiros
```

### Buscar barbeiro por ID
```bash
curl http://localhost:8080/api/barbeiros/1
```

### Buscar barbeiro por email
```bash
curl http://localhost:8080/api/barbeiros/email/carlos@barbearia.com
```

### Atualizar barbeiro
```bash
curl -X PUT http://localhost:8080/api/barbeiros/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Silva",
    "email": "carlos.silva@barbearia.com",
    "senha": "654321",
    "telefone": "21987654321",
    "ativo": true
  }'
```

### Desativar barbeiro
```bash
curl -X PATCH http://localhost:8080/api/barbeiros/1/desativar
```

### Reativar barbeiro
```bash
curl -X PATCH http://localhost:8080/api/barbeiros/1/reativar
```

### Deletar barbeiro
```bash
curl -X DELETE http://localhost:8080/api/barbeiros/1
```

## Serviços

### Criar um serviço (barbeiro)
```bash
curl -X POST "http://localhost:8080/api/servicos?barbeiroId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Corte Americano",
    "descricao": "Corte com navalha e acabamento perfeito, inclui lavagem",
    "preco": 45.00,
    "duracaoMinutos": 40,
    "ativo": true
  }'
```

### Listar serviços por barbeiro
```bash
curl http://localhost:8080/api/servicos/barbeiro/1
```

### Listar todos os serviços (incluindo inativos)
```bash
curl http://localhost:8080/api/servicos
```

### Listar apenas serviços disponíveis/ativos
```bash
curl http://localhost:8080/api/servicos/disponiveis
```

### Buscar serviço por ID
```bash
curl http://localhost:8080/api/servicos/1
```

### Buscar serviço por nome
```bash
curl http://localhost:8080/api/servicos/nome/Corte%20Americano
```

### Atualizar um serviço
```bash
curl -X PUT http://localhost:8080/api/servicos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Corte Americano Premium",
    "descricao": "Corte com navalha, produtos importados e finalização",
    "preco": 55.00,
    "duracaoMinutos": 45,
    "ativo": true
  }'
```

### Desativar um serviço
```bash
curl -X PATCH http://localhost:8080/api/servicos/1/desativar
```

### Reativar um serviço
```bash
curl -X PATCH http://localhost:8080/api/servicos/1/reativar
```

### Deletar um serviço
```bash
curl -X DELETE http://localhost:8080/api/servicos/1
```

## Agenda

### Criar agenda (para um barbeiro)
```bash
curl -X POST "http://localhost:8080/api/agendas?barbeiroId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "diaSemana": 2,
    "horaInicio": "09:00",
    "horaFim": "18:00",
    "ativo": true
  }'
```

### Listar agendas por barbeiro
```bash
curl http://localhost:8080/api/agendas/barbeiro/1
```

### Listar todas as agendas
```bash
curl http://localhost:8080/api/agendas
```

### Buscar agenda por ID
```bash
curl http://localhost:8080/api/agendas/1
```

### Listar agendas por dia da semana
```bash
curl http://localhost:8080/api/agendas/dia/2
```

### Listar apenas agendas ativas
```bash
curl http://localhost:8080/api/agendas/ativos
```

### Atualizar agenda
```bash
curl -X PUT http://localhost:8080/api/agendas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "diaSemana": 2,
    "horaInicio": "08:00",
    "horaFim": "19:00",
    "ativo": true
  }'
```

### Desativar agenda
```bash
curl -X PATCH http://localhost:8080/api/agendas/1/desativar
```

### Reativar agenda
```bash
curl -X PATCH http://localhost:8080/api/agendas/1/reativar
```

### Deletar agenda
```bash
curl -X DELETE http://localhost:8080/api/agendas/1
```

### Validar horário
```bash
curl "http://localhost:8080/api/agendas/validar?diaSemana=2&horaInicio=10:00&duracaoMinutos=60"
```

## Agendamentos

### Criar agendamento (cliente)
```bash
curl -X POST "http://localhost:8080/api/agendamentos?clienteId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "dataHoraInicio": "2025-04-10T14:00:00",
    "servicosIds": [1, 2],
    "observacoes": "Corte e barba"
  }'
```

### Buscar agendamento por ID
```bash
curl http://localhost:8080/api/agendamentos/1
```

### Listar todos os agendamentos
```bash
curl http://localhost:8080/api/agendamentos
```

### Listar agendamentos por período
```bash
curl "http://localhost:8080/api/agendamentos/periodo?dataInicio=2025-04-10&dataFim=2025-04-15"
```

### Listar agendamentos de um cliente
```bash
curl http://localhost:8080/api/agendamentos/cliente/1
```

### Listar agendamentos de um barbeiro
```bash
curl http://localhost:8080/api/agendamentos/barbeiro/1
```

### Verificar disponibilidade para um dia e serviço
```bash
curl "http://localhost:8080/api/agendamentos/disponibilidade?data=2025-04-10&servicoId=1"
```

### Verificar disponibilidade da semana
```bash
curl "http://localhost:8080/api/agendamentos/disponibilidade/semana?dataInicio=2025-04-10"
```

### Cancelar um agendamento
```bash
curl -X PATCH "http://localhost:8080/api/agendamentos/1/cancelar?motivo=Cliente%20não%20poderá%20comparecer"
```

### Cancelar agendamento (com validação de cliente)
```bash
curl -X PATCH "http://localhost:8080/api/agendamentos/1/cancelar?clienteId=1&motivo=Cliente%20não%20poderá%20comparecer"
```