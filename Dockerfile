# Use JDK for building the Maven project
FROM eclipse-temurin:17-jdk-focal as builder
WORKDIR /opt/app

# Assuming 'ticketguru' is a directory inside your project root that contains the Maven project
ARG PROJECT_ROOT="ticketguru"

# Copy the Maven wrapper and definition files
COPY ${PROJECT_ROOT}/.mvn/ .mvn/
COPY ${PROJECT_ROOT}/mvnw ${PROJECT_ROOT}/pom.xml ./

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Pre-fetch dependencies to cache them
RUN ./mvnw dependency:go-offline

# Copy your project's source code
COPY ${PROJECT_ROOT}/src ./src

# Build the project without running tests
RUN ./mvnw clean install -DskipTests

# Copy the built artifact to the final image
RUN find ./target -type f -name '*.jar' -exec cp {} /opt/app/app.jar \; -quit

# Use JRE for running the application
FROM eclipse-temurin:17-jre-alpine
COPY --from=builder /opt/app/app.jar /opt/app/

# Expose the port your app runs on
EXPOSE 8080

# Run your app
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
