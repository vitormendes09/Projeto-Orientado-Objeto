#!/bin/bash

echo " Construindo a imagem Docker da aplicação..."

# Para desenvolvimento
if [ "$1" == "dev" ]; then
    echo "Modo desenvolvimento"
    docker-compose up --build
elif [ "$1" == "prod" ]; then
    echo "Modo produção"
    docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
else
    echo "Modo padrão"
    docker-compose up --build
fi