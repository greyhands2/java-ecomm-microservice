FROM openjdk:17

LABEL authors="starq1"

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]



# A Dockerfile to build the api-gateway app and a Docker-layered file to build  a layered container that only
# rebuilds what was changes
# we are howerver going to use jibs with a maven plugin to build our docker containers instead of Dockerfile