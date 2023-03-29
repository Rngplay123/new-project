FROM openjdk:17
ADD build/libs/ClashOfMighty-1.0.0-SNAPSHOT.jar ClashOfMighty-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ClashOfMighty-1.0.0-SNAPSHOT.jar"]
EXPOSE 9000:9000
LABEL name="clashofmighty"
MAINTAINER ANUJ
