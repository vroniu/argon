FROM gradle:7.6.0-jdk11-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre

EXPOSE 8080

COPY --from=build /home/gradle/src/build/libs/argon-0.0.1.jar argon.jar
ENTRYPOINT ["java","-jar","/argon.jar"]
