# TravelingSalesman
Traveling Salesman API for University Project: Investigacion Operativa (UTN).
This back end project made with Spring Boot solves the traveling salesman algorithm.

Minimal [Spring Boot](http://projects.spring.io/spring-boot/).

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven 4](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com/invoperativa/tourisimapi/TourisimApiApplication.java` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
# API endpoint

This endpoint allows to send a list of supported locations IDs to route.

## POST

/bestRoute

## Body Example
```
{
    1,
    2,
    4,
    10,
    7
}
```

## Response Example
```
{
    1,
    2,
    4,
    10,
    7
}
```

# Supported Location IDs

ID | #0 | #1 | #2 | #3 | #4 | #5 | #6 | #7 | #8 | #9 | #10
--- | --- | --- | --- |--- |--- |--- |--- |--- |--- |--- |---
Name | Mar del Plata | Carlos Paz | Bariloche | Mendoza | Usuahia | Salta | Jujuy | Neuquen | San Luis | Rosario | San Juan
