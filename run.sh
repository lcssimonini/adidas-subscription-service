#!/bin/bash

sh build-all.sh
docker-compose -f docker-compose.yml up -d
