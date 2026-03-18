#!/bin/bash
# scripts/run-tests.sh

echo "🧪 Rodando testes da aplicação Barbearia"
echo "========================================"

echo -e "\n📦 1. Limpando e compilando..."
mvn clean compile

echo -e "\n🔍 2. Rodando testes de smoke..."
mvn test -Dtest=BarbeariaApplicationSmokeTest

echo -e "\n🔍 3. Rodando testes de controller..."
mvn test -Dtest=*ControllerTest

echo -e "\n🔍 4. Rodando testes de integração..."
mvn test -Dtest=*IntegrationTest

echo -e "\n📊 5. Gerando relatório de cobertura..."
mvn jacoco:report

echo -e "\n✅ Testes concluídos!"
echo "📈 Relatório disponível em: target/site/jacoco/index.html"