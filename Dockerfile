# --- Stage 1: Build the Java application ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application (skipping tests for faster deployment)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Java application ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the compiled JAR file from the build stage
# Note: If your pom.xml outputs a different JAR name, adjust "password-generator-*.jar" accordingly.
COPY --from=build /app/target/password-generator-*.jar app.jar

# Expose the port your application listens on (Render expects a web service to bind to a port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
