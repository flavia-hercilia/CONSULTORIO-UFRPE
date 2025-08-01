#imagem base Java para construir a aplicação
FROM openjdk:21-jdk-slim AS builder

#instala Maven dentro do contêiner de build
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

#diretório de trabalho dentro do contêiner
WORKDIR /app

COPY pom.xml .

# Baixa as dependências do Maven
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

#imagem mais leve para rodar a aplicação
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

#roda a aplicação Spring Boot quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]