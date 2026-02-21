FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# Fix permission issue
RUN chmod +x mvnw

# Build
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-Xmx512m", "-jar", "target/project-0.0.1-SNAPSHOT.jar"]