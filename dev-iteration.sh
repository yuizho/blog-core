#!/bin/bash

docker-compose down -v
./gradlew clean bootJar
docker-compose build
docker-compose up