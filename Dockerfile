# Etapa 1: Build con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 9000
EXPOSE 9000

# Variables de entorno (pueden ser sobrescritas)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://190.107.177.36:3306/cna109955_libreria
ENV SPRING_DATASOURCE_USERNAME=cna109955_admin
ENV SPRING_DATASOURCE_PASSWORD=Terry.1078

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
