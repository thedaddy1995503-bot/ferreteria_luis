# === ETAPA 1: Compilación (Builder) ===
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# === ETAPA 2: Servidor de Aplicaciones ===
FROM payara/server-full:6.2025.1-jdk17

USER root

# 1. Instalar wget y descargar el Driver de MySQL en las librerías globales de Payara
RUN apt-get update && apt-get install -y wget && \
    wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar -O ${PAYARA_DIR}/glassfish/domains/domain1/lib/ext/mysql-connector-j-8.2.0.jar && \
    apt-get clean

USER payara

# 2. Copiar tus comandos personalizados al arranque del contenedor
# Payara ejecutará esto de forma automática antes de leer tu aplicación
COPY glassfish-resources.asadmin /opt/payara/config/post-boot-commands.asadmin

# 3. Copiar el archivo .war para su despliegue masivo
# Copiamos el .war manteniendo su nombre original
COPY --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war ${PAYARA_DIR}/glassfish/domains/domain1/autodeploy/FerreteriaLuis-1.0-SNAPSHOT.war

EXPOSE 8080