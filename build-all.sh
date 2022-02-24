#!/bin/bash

cd emailservice
./gradlew clean build docker

cd ../subscription-api
./gradlew clean build docker

cd ../public-service
./gradlew clean build docker
