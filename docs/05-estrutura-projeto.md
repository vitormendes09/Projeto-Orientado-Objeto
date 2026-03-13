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