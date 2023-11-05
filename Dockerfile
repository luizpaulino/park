FROM amazoncorretto:17-alpine

WORKDIR /app

COPY /build/libs/park-0.0.1-SNAPSHOT.jar /app/app.jar
COPY /application.yml /app/application.yml

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
