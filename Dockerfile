# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set ownership to spring user
RUN chown spring:spring app.jar

# Use spring user
USER spring:spring

# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080

# Health check using wget (included in Alpine)
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
