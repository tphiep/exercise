# exercise
This is a simple project to implement REST APIs for storing and reading device data.
# System prerequisites
    1. Java 11
    2. Maven 3
    3. Docker (Docker Compose)
# Build
    1. At project root run: mvn clean package -DskipTests=true docker:build
    2. Start all services: docker-compose up (it will take about 2 minutes for all services up and running)
    3. Build e2e test at e2e folder: mvn clean package
# System 
>![Preview](https://raw.githubusercontent.com/tphiep/exercise/master/Diagram.drawio.png)
# Design 
- This project applies CQRS patterns to separate write behavior from read behavior to provides stability and scalability of the services.