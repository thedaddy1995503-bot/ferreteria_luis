# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.9.6-temurin-17 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos dependencias (esto optimiza la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y construimos el archivo .war
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Servidor Payara (equivalente a GlassFish) para ejecutar la app
FROM payara/server-full:6.2025.1-jdk17

# Copiamos el .war generado en la Etapa 1 a la carpeta de autodeploy de Payara
# Le cambiamos el nombre a ROOT.war para que la app se abra en la ruta principal (/) sin tener que escribir /FerreteriaLuis
COPY --from=builder /app/target/FerreteriaLuis-1.0-SNAPSHOT.war ${PAYARA_DIR}/glassfish/domains/domain1/autodeploy/ROOT.war

# Exponemos el puerto 8080 que es el que usa Payara por defecto
EXPOSE 8080
