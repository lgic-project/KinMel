# Use the official eclipse-temurin:17-jdk-focal image as the base
FROM eclipse-temurin:17-jdk-focal

# Set the working directory
WORKDIR /app

# Copy the compiled JAR file to the container
COPY target/kinMel-0.0.1-SNAPSHOT.jar /app/kinMel.jar

# Expose the port on which the application will run (change if your app runs on a different port)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/kinMel.jar"]