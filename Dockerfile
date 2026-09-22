# Multi-stage Docker build for AmeerRasik Mart (Render Production Deployment)

# Stage 1: Build the application using Maven and Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven POM and project sources
COPY pom.xml .
COPY src ./src

# Build the WAR package (skipping tests for fast container packaging)
RUN mvn clean package -DskipTests

# Stage 2: Production runtime environment using Apache Tomcat 9
# Note: Tomcat 9 is required for Java Servlet 4.0 (javax.servlet.*) on Java 17
FROM tomcat:9.0-jdk17-temurin

WORKDIR /usr/local/tomcat

# Remove default Tomcat web applications
RUN rm -rf webapps/*

# 1. Permanently disable Tomcat's shutdown port by setting port="-1"
#    This prevents cloud health-check requests (e.g. HEAD / HTTP/1.1) from hitting port 8005
RUN sed -i 's/<Server port="[0-9]*"/<Server port="-1"/' conf/server.xml

# 2. Deploy the generated WAR file into Tomcat's webapps directory exclusively as ROOT.war
COPY --from=builder /app/target/ameerrasikmart.war webapps/ROOT.war

# Default fallback port (Render injects its own PORT variable at runtime, e.g. 10000)
ENV PORT=8080
EXPOSE 8080

# 3. Startup command:
#    - Reads the runtime $PORT provided by Render (defaults to 8080 if unset)
#    - Configures the HTTP connector to bind explicitly to 0.0.0.0 and $PORT in server.xml
#    - Starts Tomcat in the foreground under PID 1 using exec
CMD ["sh", "-c", "PORT=${PORT:-8080} && sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT}\\\" address=\\\"0.0.0.0\\\"/g\" conf/server.xml && exec catalina.sh run"]
