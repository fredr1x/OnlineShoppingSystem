FROM alpine:latest

RUN apk add openjdk17

WORKDIR /app
COPY online-shop-system*.jar application.jar
COPY application-dev.yml .
COPY .env .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
CMD ["--spring.config.location=classpath:/application.yml,file:application-dev.yml"]