# Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia os arquivos de configuração do Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixa as dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src src

# Compila e empacota a aplicação
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final mais leve
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Cria o diretório de dados e ajusta permissões ANTES de criar o usuário
RUN mkdir -p /app/data && \
    addgroup -S spring && \
    adduser -S spring -G spring && \
    chown -R spring:spring /app

# Muda para usuário não-root
USER spring:spring

# Copia o JAR do estágio builder
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", \
    "-jar", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}", \
    "app.jar"]