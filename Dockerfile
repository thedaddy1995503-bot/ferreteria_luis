# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Servidor Payara oficial (Última versión estable que incluye Java 17)
FROM payara/server-full:latest

# Copiamos el .war generado
COPY --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war ${PAYARA_DIR}/glassfish/domains/domain1/autodeploy/ROOT.war

EXPOSE 8080