## Test task (distributed map)
Distributed storage of map objects

### How it works
System has three components: 
* master server (manages clients and workers, process commands of distributed map)
* worker (single part of distributed map - stores map entries)
* client (console application - allows to send commands to server)

Commands from clients are sent to the server. Server process commands to store data into and get data from distributed map.
* put key:value (puts entry into distributed map (key - integer, value - string))
* get key (gets value by thie given key from distributed map)
* quit (disconnects client form server)
* shutdown (shutdowns server)

#### Usage
preconditions:
* docker must be installed

usage:
* build application by running of build.sh
* start master server with the give number of workers (by default - 2 workers)
* start clients and send commands to the server

logging:
* system logs to /tmp/distributed_map.log

### Consistency guaranties
* all data stored in memeory and workers can be added/removed manually, so
  * if we stop the server, all data will be lost
  * if we manually stop some worker, all data stored in this worker will be lost
  * if we manually start new worker after server is started and some data is stored in distributed map, part of stored data will not be available
* the system has not been tested under high load

### Used documentation
* Netty in Action by Norman Maurer, Marvin Allen Wolfthal
* https://netty.io/
* https://docs.docker.com/

### Tools and third-party libraries
* Intellij Idea
* Maven
* Netty
* Spring boot
* Logback

### How long it takes
~40 hours

### Known issues
* use-case "shutdown" doesn't not work correctly (client nodes don't stop)