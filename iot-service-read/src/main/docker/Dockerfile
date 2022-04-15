FROM openjdk:11-jre-slim
RUN mkdir -p /usr/local/iotservice-rea
ADD iotserver-read-0.0.1-SNAPSHOT.jar /usr/local/iotservice-read/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh