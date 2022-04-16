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
# System 
>![Preview](https://raw.githubusercontent.com/tphiep/exercise/master/Diagram.drawio.png)
# Design 
- The project applies CQRS patterns (Single Responsibility principle of SOLID) to separate write behavior from read behavior to provide stability and scalability of these services.
## Tech Stack
- Springboot: a fast development tool for Spring based application.
- Kafka: provide high-throughput, low-latency system platform for real-time data processing.
- Mongodb: it allows storing documents with a flexible schema. It is also easy to access documents using indexing.
- Kafka and MongoDB are both distributed system, they are easy to scale.
# Summary
- Preparation: 2 hours (including setup development environment)
- Coding: 7 hours (including tests, fixing bugs, refactoring code)
- Documentation: 30 minutes
- Building and testing: 30 minutes
- Grand total: 10 hours