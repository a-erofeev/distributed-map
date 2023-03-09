FROM local_maven:3.9.0-jdk-17
CMD /opt/java/openjdk/bin/java -cp target/test_task_huawei-1.0-SNAPSHOT-spring-boot.jar -Dloader.main=test.huawei.dmap.client.ClientNodeApp org.springframework.boot.loader.PropertiesLauncher
