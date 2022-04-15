#!/bin/sh
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -jar /usr/local/iotservice-read/@project.build.finalName@.jar