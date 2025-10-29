# Stage 1: Build the jar
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only pom first for caching
COPY pom.xml .
# Copy the source code
COPY src ./src

# Build the project (creates target/*.jar)
RUN mvn clean package -DskipTests

# Stage 2: Run the jar
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from build stage (handles any version automatically)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

