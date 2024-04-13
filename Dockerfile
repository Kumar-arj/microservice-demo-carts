FROM openjdk:17-jdk-alpine3.14

WORKDIR /usr/src/app
COPY ./target/*.jar ./app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","./app.jar", "--port=80"]
