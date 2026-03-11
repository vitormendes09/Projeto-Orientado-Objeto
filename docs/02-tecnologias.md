# Stack Tecnológica

## Tecnologias Principais

### Backend
| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.4 | Framework base |
| Spring Web MVC | 3.2.4 | API REST |
| Spring Data JPA | 3.2.4 | Persistência |
| Spring Validation | 3.2.4 | Validação de dados |
| Spring Actuator | 3.2.4 | Monitoramento |

### Build e Dependências
| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Maven | 3.9.6 | Gerenciamento de dependências |
| Lombok | 1.18.30 | Redução de boilerplate |
| MapStruct | 1.5.5.Final | Mapeamento DTO/Entity |
| JaCoCo | 0.8.11 | Cobertura de testes |

### Banco de Dados
| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| H2 Database | 2.2.224 | Banco em memória/arquivo |
| H2 Console | - | Interface de administração |

## Containerização

### Docker
| Componente | Versão | Propósito |
|------------|--------|-----------|
| Docker Engine | 24.0+ | Container runtime |
| Docker Compose | 2.20+ | Orquestração local |
| eclipse-temurin:17-jre-alpine | 17 | Imagem base Java |
| maven:3.9.6-eclipse-temurin-17 | 3.9.6 | Build stage |

### Serviços Auxiliares
| Serviço | Versão | Propósito |
|---------|--------|-----------|
| Adminer | latest | Interface web para BD |

## Testes

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Spring Boot Test | 3.2.4 | Testes de integração |
| REST Assured | 5.3.2 | Testes de API |
| Testcontainers | 1.19.5 | Testes com containers |
| JUnit Jupiter | 5.10.2 | Framework de testes |
| JaCoCo | 0.8.11 | Relatório de cobertura |

## Ferramentas de Desenvolvimento

| Ferramenta | Propósito |
|------------|-----------|
| Spring Boot DevTools | Auto-reload durante desenvolvimento |
| Lombok | Redução de código boilerplate |
| Maven Compiler Plugin | Compilação com processamento de anotações |
| Maven Surefire Plugin | Execução de testes |

## Monitoramento

| Componente | Propósito |
|------------|-----------|
| Spring Boot Actuator | Endpoints de monitoramento |
| Health Checks | Verificação de saúde da aplicação |
| Métricas | Coleta de métricas da aplicação |

## Por que essas escolhas?

### Spring Boot 3.2.4
- **Estabilidade**: Versão madura do ecossistema
- **Suporte**: Long-term support da VMware
- **Comunidade**: Ampla documentação e suporte

### Java 17
- **LTS**: Suporte de longo prazo
- **Performance**: Melhorias significativas
- **Recursos**: Records, pattern matching, etc.

### Docker Multi-stage
- **Tamanho**: Imagem final ~200MB vs 700MB
- **Segurança**: Usuário não-root
- **Cache**: Build mais rápido

### H2 Database
- **Simplicidade**: Zero configuração
- **Portabilidade**: Arquivo único
- **Evolução**: Fácil migração para PostgreSQL

### MapStruct
- **Performance**: Compile-time vs runtime reflection
- **Type-safe**: Erros em tempo de compilação
- **Manutenção**: Código gerado explícito