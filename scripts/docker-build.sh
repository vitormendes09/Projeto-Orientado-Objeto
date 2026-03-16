#!/bin/bash

echo "==================================="
echo " Construindo a imagem Docker da aplicação..."
echo "==================================="

# Para desenvolvimento
if [ "$1" == "dev" ]; then
    echo " Modo desenvolvimento"
    docker-compose up --build
elif [ "$1" == "prod" ]; then
    echo " Modo produção"
    docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
else
    echo "Modo padrão"
    docker-compose up --build
fi

# Verificar se subiu corretamente
if [ $? -eq 0 ]; then
    echo "==================================="
    echo " Aplicação iniciada com sucesso!"
    echo " API: http://localhost:8080"
    echo "  Adminer: http://localhost:8081"
    echo " H2 Console: http://localhost:8080/h2-console"
    echo "==================================="
else
    echo " Erro ao iniciar a aplicação"
    exit 1
fi