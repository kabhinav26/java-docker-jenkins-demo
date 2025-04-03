# Use a multi-stage build to keep the final image small
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Create the final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Add health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8083/api/health || exit 1

# Expose the port
EXPOSE 8083

# Add debug logging
ENV JAVA_OPTS="-Dserver.port=8083 -Dlogging.level.org.springframework=DEBUG"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 