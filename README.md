# Url-Shortener

This project was built using JDK version 8 and Maven 3.9.4

## Build

Run `mvn clean install` to build the project (running unit/integration tests within). The executable jar (url-shortener-0.0.1-SNAPSHOT.jar) will be generated under ./target.

## Running the API

To run the API, just lauch the executable jar using: java -jar url-shortener-0.0.1-SNAPSHOT

You can access the H2 console under http://localhost:8080/h2-console, and the swagger interface under http://localhost:8080/swagger-ui/

## Persistence

The H2 database is persisted using the data/sampledata.mv.db file, so original and shortened URLs are kept after API restart.

## Code Coverage

Notice that the code is mostly 100% covered by unit/integration tests.

![image](https://github.com/aymenbendhia/url-shortener/assets/90474099/347b11ff-2fcd-4aaa-96fa-076aca530424)

