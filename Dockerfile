# Build stage
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY system.properties .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
VOLUME /data

# Copy the jar with explicit name
COPY --from=build /app/target/my-java-project-1.0-SNAPSHOT.jar /app/app.jar
COPY data/goodlisteners.db /data/goodlisteners.db

# Create directory for the database with proper permissions
RUN mkdir -p /data && chmod 777 /data

ENTRYPOINT ["java", "-jar", "/app/app.jar"]