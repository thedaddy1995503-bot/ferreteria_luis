# === ETAPA 1: Compilación (Builder) ===
FROM maven:3.8.5-openjdk-17 AS builder
ENV MAVEN_OPTS="-Xmx300m -XX:MaxMetaspaceSize=128m"
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# === ETAPA 2: Servidor de Aplicaciones ===
FROM payara/server-full:6.2025.1-jdk17

# Copiamos el conector de MySQL desde la etapa de construcción a las librerías del dominio de Payara
COPY --chown=payara:payara --from=builder /root/.m2/repository/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar /opt/payara/appserver/glassfish/domains/domain1/lib/

# Copiar el archivo .war al directorio de despliegue estándar (ROOT.war)
# El script de entrada de Payara lo desplegará automáticamente y leerá payara-resources.xml.
COPY --chown=payara:payara --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war /opt/payara/deployments/ROOT.war

EXPOSE 8080