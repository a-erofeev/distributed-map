#!/bin/sh

docker run -it --net aerofeev-test-net -v /tmp:/tmp -v "$(pwd)":/home/test client:1.0 

