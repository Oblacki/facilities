FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/facilities-2.5.0-SNAPSHOT.jar /app

EXPOSE 8084

CMD ["java", "-jar", "facilities-2.5.0-SNAPSHOT.jar"]