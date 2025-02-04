FROM openjdk:17
EXPOSE 80
ARG JAR_FILE=/build/libs/team6-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","-Dspring.profiles.active=dev","/app.jar"]
