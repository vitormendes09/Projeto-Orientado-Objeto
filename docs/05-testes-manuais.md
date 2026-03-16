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

### Buscar cliente por telefone 

```bash

    curl http://localhost:8080/api/clientes/telefone/22998041266
```

## Serviços 

### Criar um serviço (barbeiro)

```bash
curl -X POST http://localhost:8080/api/servicos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Corte Americano",
    "descricao": "Corte com navalha e acabamento perfeito, inclui lavagem",
    "preco": 45.00,
    "duracaoMinutos": 40
  }'
```

### Listar todos os serviços (incluindo inativos) - para barbeiro
``` bash

curl http://localhost:8080/api/servicos
```

### Listar apenas serviços disponíveis/ativos - para clientes
```bash

curl http://localhost:8080/api/servicos/disponiveis
```

### Buscar serviço por ID
```bash

( Substitua 1 pelo ID desejado )
curl http://localhost:8080/api/servicos/1
```

### Buscar serviço por nome
``` bash

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
    "duracaoMinutos": 45
  }'
```

### Desativar um serviço (soft delete) 
``` bash

curl -X PATCH http://localhost:8080/api/servicos/2/desativar
```

### Reativar um serviço
```bash

curl -X PATCH http://localhost:8080/api/servicos/2/reativar
```

## Agendamentos

### Configurar agenda (horário de funcionamento)

```bash
curl -X POST http://localhost:8080/api/agendas \
  -H "Content-Type: application/json" \
  -d '{
    "diaSemana": 2,
    "horaInicio": "09:00",
    "horaFim": "18:00",
    "ativo": true
  }'
  ```

### Verificar disponibilidade para um serviço

``` bash
curl "http://localhost:8080/api/agendamentos/disponibilidade?data=2025-04-10&servicoId=1"
```

### Verificar disponibilidade da semana

``` bash
curl "http://localhost:8080/api/agendamentos/disponibilidade/semana?dataInicio=2025-04-10"
```

### Listar todos os agendamentos

``` bash
curl http://localhost:8080/api/agendamentos
```

### Listar agendamentos por período

```bash
curl "http://localhost:8080/api/agendamentos/periodo?dataInicio=2025-04-10&dataFim=2025-04-15"
```

### Listar agendamentos de um cliente

``` bash
curl http://localhost:8080/api/agendamentos/cliente/1
```

### Cancelar um agendamento

``` bash
curl -X PATCH "http://localhost:8080/api/agendamentos/1/cancelar?motivo=Cliente%20não%20poderá%20comparecer"
```

### Buscar agendamento por ID

``` bash 
curl http://localhost:8080/api/agendamentos/1
```