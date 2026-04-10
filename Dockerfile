FROM gradle:8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:17-jre
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]
