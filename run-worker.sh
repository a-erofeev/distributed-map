#!/bin/sh
#$JAVA_HOME/bin/java -cp target/test_task_huawei-1.0-SNAPSHOT-spring-boot.jar -Dloader.main=test.huawei.dmap.worker.WorkerNodeApp org.springframework.boot.loader.PropertiesLauncher

docker run -it --net aerofeev-test-net -v /tmp:/tmp -v "$(pwd)":/home/test worker:1.0 

