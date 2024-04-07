# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-focal as builder

# Set the working directory
WORKDIR /opt/app

# Copy Maven wrapper files and pom.xml
COPY ticketguru/mvnw ./
COPY ticketguru/pom.xml ./
COPY ticketguru/.mvn/ ./

RUN ls -a
RUN ls -a .mvn/

# Make Maven wrapper executable and download dependencies
RUN chmod +x mvnw
#RUN ./mvnw dependency:go-offline

# Copy application source
COPY ticketguru/src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Stage 2: Create runtime image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /opt/app

# Copy JAR file from the builder stage
COPY --from=builder /opt/app/ticketguru/target/*.jar ./app.jar

# Expose port 8080
EXPOSE 8080

# Define entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]