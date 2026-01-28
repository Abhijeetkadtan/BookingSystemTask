# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom first (better caching)
COPY pom.xml .

# Download dependencies
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Package application
RUN mvn clean package -DskipTests


# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Create folder for H2 file DB
RUN mkdir -p /app/data

# Copy jar from build stage
COPY --from=builder /build/target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Run the app
CMD ["sh", "-c", "java -jar app.jar"]
