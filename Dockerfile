# Use AdoptOpenJDK as base image
FROM openjdk:17-oracle

# Set working directory
WORKDIR /app

# Copy JAR file into container
COPY app-controller/target/martapp.jar /app/martapp.jar

# Expose port
EXPOSE 8080

# Set JVM options
ENV JAVA_OPTS=""

# Command to run the application
CMD java $JAVA_OPTS -jar martapp.jar