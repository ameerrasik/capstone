# Multi-stage Docker build for AmeerRasik Mart (Render Deployment)
# Stage 1: Build the application using Maven and Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven POM and project sources
COPY pom.xml .
COPY src ./src

# Build the WAR application (skipping unit tests for fast Docker build)
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment using Apache Tomcat 9
# Note: Tomcat 9 is required for Java Servlet 4.0 (javax.servlet.*) on Java 17
FROM tomcat:9.0-jdk17-temurin

WORKDIR /usr/local/tomcat

# Remove default Tomcat web applications for a clean deployment
RUN rm -rf webapps/*

# Deploy the generated WAR file into Tomcat's webapps directory as ROOT.war (and context path /ameerrasikmart)
COPY --from=builder /app/target/ameerrasikmart.war webapps/ROOT.war
COPY --from=builder /app/target/ameerrasikmart.war webapps/ameerrasikmart.war

# Default port configuration
ENV PORT=8080
EXPOSE 8080

# Dynamically configure Tomcat to bind to 0.0.0.0 and listen on the PORT provided by Render
CMD ["sh", "-c", "PORT=${PORT:-8080} && sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT}\\\" address=\\\"0.0.0.0\\\"/g\" conf/server.xml && exec catalina.sh run"]
