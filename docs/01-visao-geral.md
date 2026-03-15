# Visão Geral

Este documento consolida a visão geral do sistema e todas as regras de negócio (RN) da aplicação. Serve como fonte única da verdade para entendimento do projeto e para validação das implementações.

## Arquitetura

O sistema segue os princípios da Arquitetura Hexagonal (Clean Architecture), com clara separação entre:

    Core/Domínio: Regras de negócio e entidades

    Aplicação: Orquestração dos casos de uso

    Infraestrutura: Detalhes técnicos (banco de dados, web, etc.)

    Shared: Padrões de projeto reutilizáveis

## Padrões de Projeto Implementados


| Padrão | Aplicação | Benefício |
|---------|-----------|-----------|
| Factory | Criação de entidades  | Encapsulamento da lógica de criação |
| Prototype | Clonagem para backup antes de atualizações | Segurança e auditoria de mudanças |
| Visitor | Estatísticas e logs (ClienteVisitor, ServicoVisitor) | Separação de algoritmos dos objetos |