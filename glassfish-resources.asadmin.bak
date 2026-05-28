# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Servidor Payara oficial
FROM payara/server-full:latest

# Copiamos el .war manteniendo su nombre original dentro de la ruta nativa de despliegue
COPY --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war ${DEPLOY_DIR}/

EXPOSE 8080