# exercise
exercise
# System prerequisites
    1. Java 8
    2. Maven 3
    3. Docker (Docker Compose)
# Build
    1. At project root folder run: mvn clean package -DskipTests=true docker:build
    2. Start all services: docker-compose up
    3. Build e2e test at e2e folder: mvn clean package
# System 
>![Preview](https://raw.githubusercontent.com/tphiep/exercise/master/Diagram.drawio.png)
