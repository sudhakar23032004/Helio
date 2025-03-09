FROM amazoncorretto:21.0.4-alpine3.18
WORKDIR /app

# Copy the JAR file (/app)
COPY /build/libs/Helio-0.0.1-SNAPSHOT.jar ./Helio.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "Helio.jar"]