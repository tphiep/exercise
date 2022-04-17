# exercise
This is a simple project to implement REST APIs for storing and reading device data.
# System prerequisites
    1. Java 11
    2. Maven 3
    3. Docker (Docker Compose)
# Build
    1. At project root run: mvn clean package -DskipTests=true docker:build
    2. Start all services: docker-compose up (it will take about 2 minutes for all services up and running)
    3. Build e2e test: mvn clean package
# Service endpoints
- POST
```
curl -d '{"deviceId": "e3d4686d-5d2a-42c7-a471-1efb761646d4", "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "C", "value": "23.3"}}}' -H "Content-Type: application/json" -X POST http://localhost:8089/api/devices
{"deviceId":"e3d4686d-5d2a-42c7-a471-1efb761646d4","timestamp":"2022-04-17T04:53:01.410Z"}
curl -d '{"deviceId": "e3d4686d-5d2a-42c7-a471-1efb761646d4", "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "F", "value": "223.3"}}}' -H "Content-Type: application/json" -X POST http://localhost:8089/api/devices
{"deviceId":"e3d4686d-5d2a-42c7-a471-1efb761646d4","timestamp":"2022-04-17T04:53:27.797Z"}
```
- GET
```
curl -H "Content-Type: application\json" -X GET 'http://localhost:8090/api/devices/e3d4686d-5d2a-42c7-a471-1efb761646d4?fromDate=2022-04-16T04:49:08.102Z&toDate=2023-04-17T04:49:08.102Z'
{
  "deviceId": "e3d4686d-5d2a-42c7-a471-1efb761646d4",
  "longitude": -120.9762,
  "latitude": 41.25,
  "data": [
    {
      "humidity": 123,
      "temperature": {
        "unit": "C",
        "value": "23.3"
      },
      "timestamp": "2022-04-17T04:53:01.410Z"
    },
    {
      "humidity": 123,
      "temperature": {
        "unit": "F",
        "value": "223.3"
      },
      "timestamp": "2022-04-17T04:53:27.797Z"
    }
  ]
}
```
# System 
>![Preview](https://raw.githubusercontent.com/tphiep/exercise/master/Diagram.drawio.png)
# Design 
- The project applies CQRS patterns (Single Responsibility principle of SOLID) to separate write behavior from read behavior to provide stability and scalability of these services.
## Tech Stack
- Springboot: a fast development tool for Spring based application. Spring framework provides IoC container that is an implementation of DI principle, it helps to make the code cleaner. 
- Kafka: provide high-throughput, low-latency system platform for real-time data processing.
- Mongodb: it allows storing documents with a flexible schema. It is also easy to access documents using indexing.
- Kafka and MongoDB are both distributed system, they are easy to scale.
# Summary
- Preparation: 2 hours (including setup development environment)
- Coding: 7 hours (including tests, fixing bugs, refactoring code)
- Documentation: 30 minutes
- Building and testing: 30 minutes
- Grand total: 10 hours