#!/bin/sh

#docker run -it -h master_host --net aerofeev-test-net -v /tmp:/tmp -v "$(pwd)":/home/test master_server:1.0

if [ -z "$1" ] 
then
	worker_num=2
else
	worker_num=$1
fi
echo Server will start with $worker_num workers

docker-compose up --scale worker=$worker_num
