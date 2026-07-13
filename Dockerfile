FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/home4paws-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Cap the heap so the JVM plays nice on a small (~1GB) instance
ENTRYPOINT ["java","-XX:MaxRAMPercentage=60","-jar","app.jar"]