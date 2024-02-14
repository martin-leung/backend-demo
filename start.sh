#!/bin/bash

# Prompt the user for the API key
read -p "Enter your Google Maps API key: " apiKey

# Build the Docker image
docker build -t martapp .

# Run the Docker container with the provided API key as a JVM argument
docker run -p 8080:8080 -e "JAVA_OPTS=-Dapi.key=$apiKey" martapp