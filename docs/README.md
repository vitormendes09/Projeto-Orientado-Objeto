# Barbearia API - Documentação do Projeto

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen)
![Java](https://img.shields.io/badge/Java-17-blue)
![Docker](https://img.shields.io/badge/Docker-24.0-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## Sobre o Projeto

API RESTful para gerenciamento de barbearia, desenvolvida com Spring Boot, seguindo boas práticas de desenvolvimento, arquitetura limpa e deploy containerizado.

## Principais Características

- **API RESTful** com endpoints bem definidos
- **Containerização** com Docker para ambientes consistentes
- **Perfis de ambiente** (dev/prod) para diferentes configurações
- **Documentação completa** para fácil manutenção
- **Factory Visitor Prototype** como padrões de projeto
- **Pronto para testes** com estrutura preparada

## Índice da Documentação

| Arquivo | Descrição |
|---------|-----------|
| [01-visao-geral.md](./01-visao-geral.md) | Visão geral |
| [02-tecnologias.md](./02-tecnologias.md) | Stack tecnológica detalhada |
| [03-configuracao.md](./03-configuracao.md) | Configurações do projeto |
| [04-estrutura-projeto.md](./04-estrutura-projeto.md) | Organização do código fonte |
| [05-testes-manuais.md](05-testes-manuais.md) | Testes manuais utilizando curl |
| [09-testes.md](./09-testes.md) | Estratégias e tipos de testes |


## Objetivos da Documentação

Esta documentação visa:

1. **Facilitar a avaliação** do projeto por outros desenvolvedores
2. **Garantir a reprodutibilidade** do ambiente em qualquer máquina
3. **Documentar decisões técnicas** e suas justificativas
4. **Fornecer guias práticos** para desenvolvimento e deploy
5. **Estabelecer padrões** para futuras contribuições

## Primeiros Passos

```bash
# Clone o repositório
git clone https://github.com/vitormendes09/Projeto-Orientado-Objeto

# Execute com Docker (recomendado)
./scripts/docker-build.sh dev

# Ou acesse via PowerShell
./scripts/docker-build.ps1 dev

```

# Acesse a aplicação: 
http://localhost:8080

# Ver logs
docker-compose logs -f barbearia-api

# Parar serviços
docker-compose down

# Parar e remover volumes (limpa dados)
docker-compose down -v

# Executar comando no container
docker exec -it barbearia-api sh

# Ver status
docker-compose ps