#1st stage - build
# base image with JDK to build and run java app
FROM maven:3.9.9-eclipse-temurin-23 AS builder

ARG COMPILE_DIR=/compiledir

# directory where src code resides
WORKDIR ${COMPILE_DIR}

# copy required files into the image
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src
COPY todos.json /app/todos.json


# package app using run directive
# this will download dependencies in pom.xml
# compile and package in jar
RUN mvn package -Dmaven.test.skip=true

#2nd stage run
FROM maven:3.9.9-eclipse-temurin-23
ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}
COPY --from=builder /compiledir/target/ssf_practice_workshop-0.0.1-SNAPSHOT.jar ssf_practice_workshop.jar
ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}
ENTRYPOINT [ "java", "-jar", "ssf_practice_workshop.jar" ]
