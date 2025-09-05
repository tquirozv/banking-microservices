# Multi-stage build for optimized image
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# ========================================
# RUNTIME STAGE
# ========================================

FROM eclipse-temurin:17-jre-alpine

# Install essential packages
RUN apk add --no-cache \
    curl \
    bash \
    && rm -rf /var/cache/apk/*

# Create application user for security
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/account-service-*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && \
    chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# JVM optimization and configuration
ENV JAVA_OPTS="-server \
    -Xms512m \
    -Xmx1024m \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:+UseCompressedOops \
    -Djava.security.egd=file:/dev/./urandom"

# Application startup command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# ========================================
# BUILD AND RUN COMMANDS
# ========================================

# Build the Docker image:
# docker build -t account-service:1.0.0 .

# Run the container:
# docker run -d \
#   --name account-service \
#   -p 8081:8081 \
#   -e DB_HOST=postgres \
#   -e DB_PORT=5432 \
#   -e DB_NAME=account_service_db \
#   -e DB_USERNAME=postgres \
#   -e DB_PASSWORD=password \
#   --network banking-network \
#   account-service:1.0.0

# Run with resource limits:
# docker run -d \
#   --name account-service \
#   --memory=1g \
#   --cpus=1.0 \
#   --restart=unless-stopped \
#   -p 8081:8081 \
#   account-service:1.0.0