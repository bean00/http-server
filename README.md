# HTTP Server

A project that implements a basic HTTP server, built using Java. It's meant to serve up both static and dynamic websites. The behavior of the server is verified using Cob Spec (see Run Acceptance Tests section below).

The HTTP Server supports the following:
1. HTTP Methods: simple GET and HEAD.

## Getting Started
Clone this repository to your local machine
<br>
```$ git clone https://github.com/bean00/http-server.git```
<br>
```$ cd http-server```

## Dependencies
1. [Java 1.8](http://docs.oracle.com/javase/8/docs/)
2. [JUnit 5.0](http://junit.org/junit5/docs/current/user-guide/)
3. [Maven](https://maven.apache.org)

## Run Tests
Run the following command to run the unit tests: <br>
```$ mvn test```

## Run Acceptance Tests
The acceptance test and instructions to run them can be found [here](https://github.com/8thlight/cob_spec) 

## Compile and Build Package
At the root dir run the command:<br>
```$ mvn package```

## Run the Server
- Run the command below from the root directory
- Pass in a flag and an argument for the directory to serve files from (```[-d DIRECTORY]```)
- Pass in an *optional flag and argument for the port to listen on (```[-p PORT]```)
    - *The default port is 5000 
- You can enter the arguments in any order

Proper usage:<br>
```$ java -jar [SERVER JAR FILE] [-d DIRECTORY]```<br>
```$ java -jar [SERVER JAR FILE] [-p PORT] [-d DIRECTORY]```<br>
Example:<br>
```$ java -jar target/http-server-1.0-SNAPSHOT-jar-with-dependencies.jar -d public```<br>
```$ java -jar target/http-server-1.0-SNAPSHOT-jar-with-dependencies.jar -p 8080 -d public```
