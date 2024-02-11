# Use AdoptOpenJDK as base image
FROM openjdk:17-oracle

# Set working directory
WORKDIR /app

# Copy JAR file into container
COPY target/martapp.jar /app/martapp.jar

# Expose port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "martapp.jar"]