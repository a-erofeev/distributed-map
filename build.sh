#!/bin/sh
docker build --tag local_maven:3.9.0-jdk-17 . -f maven.Dockerfile

docker build --tag master_server:1.0 . -f master.Dockerfile
docker build --tag client:1.0 . -f client.Dockerfile
docker build --tag worker:1.0 . -f worker.Dockerfile

docker network create aerofeev-test-net

docker run -it -v "$(pwd)":/home/test local_maven:3.9.0-jdk-17 mvn clean package

