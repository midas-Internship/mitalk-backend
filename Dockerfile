FROM adoptopenjdk/openjdk11:ubi

EXPOSE 8080
ENV TZ=Asia/Seoul

COPY ./build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]