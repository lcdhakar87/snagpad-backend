# Step 1: Base image jisme Java 21 installed ho
FROM eclipse-temurin:21-jdk-alpine

# Step 2: Working directory set karein
WORKDIR /app

# Step 3: Maven build se bani .jar file ko container mein copy karein
COPY target/*.jar app.jar

# Step 4: Application ko run karne ki command
ENTRYPOINT ["java", "-jar", "app.jar"]
