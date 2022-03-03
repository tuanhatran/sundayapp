#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install
#
# Package stage
#
FROM openjdk:11-jdk as run
VOLUME /tmp
COPY --from=build /home/app/target/technicaltest.jar technicaltest.jar
COPY  /src/main/resources/service-account.json /home/app/service-account.json
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/technicaltest.jar"]
