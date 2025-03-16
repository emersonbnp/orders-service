# Dockerfile to build and run orders-service application
FROM gradle:jdk23-corretto-al2023 as build

WORKDIR /app

COPY . /app

RUN chmod +x gradlew

# This dockerfile is intentionally building the application inside the container.
# This is a bad idea in a real world application!

RUN ./gradlew build -x test

FROM gradle:jdk23-corretto-al2023 as run

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080:8080

# Run the application
CMD ["java", "-jar", "app.jar"]