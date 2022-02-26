#!/bin/bash

cd emailservice
./gradlew clean build docker

cd ../subscription-api
./gradlew clean build docker

cd ../publicservice
./gradlew clean build docker
