FROM eclipse-temurin:25-jdk-noble AS build
WORKDIR /workspace

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY src/ src/
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jdk-noble
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar cta-tracker.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "cta-tracker.jar"]