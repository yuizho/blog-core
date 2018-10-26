FROM openjdk:8-jdk-alpine
VOLUME /var/tmp/static
COPY build/libs/blog-core-0.1.0.jar app.jar
COPY wait.sh /wait.sh
EXPOSE 8080
ENTRYPOINT ["sh", "/wait.sh", "db", "test", "test", "java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=devdocker"]