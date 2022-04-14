#!/bin/sh
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -jar /usr/local/iotservice/@project.build.finalName@.jar