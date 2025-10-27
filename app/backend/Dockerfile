# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only pom.xml first for caching
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the project (creates target/*.jar)
RUN mvn clean package -DskipTests

# Stage 2: Run the jar
FROM eclipse-temurin:21-jre

WORKDIR /app

# Create a non-root user and group
RUN groupadd --system appuser && useradd --system --create-home --gid appuser appuser

# Copy jar from build stage (handles any version automatically)
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

EXPOSE 80

CMD ["java", "-jar", "app.jar"]

