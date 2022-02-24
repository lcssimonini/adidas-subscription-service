#!/bin/bash

docker-compose -f docker-compose.yml down
docker image rm public-service:0.0.1-SNAPSHOT subscription-api:0.0.1-SNAPSHOT email-service:0.0.1-SNAPSHOT
