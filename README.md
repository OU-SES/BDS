# BFSserver

Broker Domain Services (BDS) is a Spring Boot application designed to run as a backend server. This project is built using Maven and adheres to standard Spring Boot conventions. 

## Prerequisites

Ensure the following are installed on your machine:

- Java 11 or higher (recommended: OpenJDK 11+)
- Maven 3.6 or higher
- Git (optional, for version control)

## Build & Run Instructions

### 1. Clone the Repository

git clone https://github.com/OU-SES/BDS

cd BFSserver/BFSserver

### 2A. Build the Application through MVN

```
mvn clean install
```

This will download all dependencies and build the `.jar` file in the `target/` directory.

If you're using IntelliJ IDEA, follow these steps to open and run the project:

### 2B. Build and Run with IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click File > Open...
3. Navigate to the `BFSserver/BFSserver` directory (where the `pom.xml` is located), and select it.
4. IntelliJ will automatically detect it's a Maven project and import all dependencies.
5. Once indexing is complete, locate the main class with the `@SpringBootApplication` annotation (usually in `src/main/java/...`) and right-click it.
6. Click Run to start the server.
Note: If you're using IntelliJ IDEA, you do not need to run `mvn clean install` separately — the IDE will handle build and dependency resolution automatically.

### 3. Run the Application (if you build the application with 2B)

mvn spring-boot:run

Or, run the JAR directly:

java -jar target/*.jar

## Configuration

The default configuration is set in `src/main/resources/application.properties`.

If needed, you can override properties like server port and database settings there:

Example:

server.port=8080  
spring.datasource.url=jdbc:mysql://localhost:3306/your_db  
spring.datasource.username=root  
spring.datasource.password=your_password

## Project Structure

- src/main/java – Java source code
- src/main/resources – Configuration and resources
- pom.xml – Maven project descriptor

## Initial broker setup

1. Open BfSserverApplication.java
2. Add broker entries to the `Object[][] data` array located in the `init()` method.
3. The structure of Broker Object data is {1.Broker Name, 2.IP address of the Broker, 3. Port, 4. The number of connected clients, 5. fix quality, 6. number of satellite, 7. HDOP, 8. altitude, 9. height, 10. location(optional)}

```java
Object[][] data = {
	      {"Broker1", "127.0.0.1", "1", 0, 4, 1.5f, 100.0f, -20.0f, 40.757271, -73.990000},
	      {"Broker2", "127.0.0.2", "2", 1, 4, 1.5f, 100.0f, -20.0f, 40.757218, -73.990007}
	  };
```

4. Add as many broker entries as needed for testing. Please ensure that longitude and latitude values are accurate. The server will automatically select the broker closest to the client.
If Broker Objects are added successfully, you will see the following screen:
![Architecture Diagram](https://github.com/OU-SES/BDS/blob/main/running%20example.png)
