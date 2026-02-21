# Use official Java image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Tell Docker which port the app runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "target/project-0.0.1-SNAPSHOT.jar"]