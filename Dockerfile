# === ETAPA 1: Compilación (Builder) ===
FROM maven:3.8.5-openjdk-17 AS builder
ENV MAVEN_OPTS="-Xmx300m -XX:MaxMetaspaceSize=128m"
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# === ETAPA 2: Servidor de Aplicaciones ===
FROM payara/server-full:6.2025.1-jdk17

# Copiamos el conector de MySQL desde la etapa de construcción a las librerías del dominio de Payara
COPY --chown=payara:payara --from=builder /root/.m2/repository/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar ${PAYARA_DIR}/glassfish/domains/domain1/lib/

# Copiar tus comandos personalizados al arranque del contenedor
COPY --chown=payara:payara glassfish-resources.asadmin /opt/payara/config/post-boot-commands.asadmin

# Copiar el archivo .war para su despliegue como aplicación principal (ROOT.war)
COPY --chown=payara:payara --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war ${PAYARA_DIR}/glassfish/domains/domain1/autodeploy/ROOT.war

EXPOSE 8080