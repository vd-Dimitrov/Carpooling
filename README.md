# Carpooling 

## Description

Carpooling is an application where drivers can post about their upcoming trips and potential passengers can apply to join them on the road. 
The application consists of the travel browser, where users can check and apply for someone's trip, and the option to post one yourself. 

## Project setup

 - Head to `application.properties` and adjust `url`, `username` and `password` for your personal database settings. 
The file can be found in `carpooling/src/main/resources/application.properties`.
 - Setup the forum database by connecting your database and using `create.sql` and `generate.sql` to form the database and fill it with basic data. It can be found in `Carpooling/db`
 - For authentication this project uses Basic Authentication. In Http Headers set a Key: `Authorization` with value: `username password`. You can find valid authentications in  `insert_data.sql`.

  
## Database relations
A picture illustrating them is found in `db/relations.png`

## Technologies used
 - Java
 - Spring Boot 
 - Hibernate
 - REST API
 - MariaDB
 - Gradle
 - Thymeleaf 

## Technologies to do 
 - Swagger

## Swagger
After starting the project you can head to [Swagger Docs](http://localhost:8080/swagger-ui/index.html) and check the REST functionalities
