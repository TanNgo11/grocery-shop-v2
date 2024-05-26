# Use a specific tag for Maven and JDK to ensure compatibility
FROM maven:3.8.5-openjdk-17

# Set the working directory inside the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Run Maven clean and install to build the application
RUN mvn clean install -DskipTests

# Command to run the application
CMD ["mvn", "spring-boot:run"]