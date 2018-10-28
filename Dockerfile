FROM openjdk:8-jdk-alpine
COPY build/libs/blog-core-0.1.0.jar app.jar
RUN mkdir /var/tmp/static
RUN apk add --no-cache mysql-client
COPY wait.sh /wait.sh
EXPOSE 8080

# TODO: when use "-Djava.security.egd=file:/dev/./urandom" option, error occurred.
ENTRYPOINT ["sh", "/wait.sh", "db", "test", "test", "java", "-jar","/app.jar", "--spring.profiles.active=devdocker"]