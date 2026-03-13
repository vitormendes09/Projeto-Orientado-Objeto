# Esturura do src 

src/main/java/iff/edu/br/barbearia/
├── BarbeariaApplication.java
├── core/
│   ├── domain/
│   │   └── entities/
│   │                                     # Entidade JPA
│   ├── dtos/
│   │   ├── RequestDTO.java               # Entrada (criação/atualização)
│   │   └── ResponseDTO.java              # Saída (respostas da API)
│   ├── exceptions/
│   │   ├── BusinessException.java
│   │   └── ResourceNotFoundException.java   
│   └── ports/
│       ├── incoming/
│       │                                  # Interface do serviço
│       └── outgoing/
│                                          # Interface do repositório
├── application/
│   └── services/
│                                          # Implementação do serviço
├── infrastructure/
│   ├── persistence/
│   │                                      # Implementação JPA
│   └── web/
│       └── controllers/
│                                          # REST API
├── shared/
│   └── patterns/
│       ├── factory/
│       │                                  # Padrão Factory
│       ├── prototype/
│       │                                  # Padrão Prototype
│       └── visitor/
│                                          # Padrão Visitor 
│           
└── config/
                                           # Configuração do MapStruct