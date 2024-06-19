# Tennis API implementation

This project implements the assignment that InQool provides as an entry project 
for Java developer position. Project implements all tasks from the assignment 
(+ all bonus tasks).

## Configuration
Configuration is in `application.properties` file.
* jwt.secret - secret key for JWT (256-bit key)
* jwt.expiration.accessToken - expiration time for access token in minutes
* jwt.expiration.refreshToken - expiration time for refresh token in minutes
* app.insert-data - if true, inserts 4 courts with 4 different surfaces
* app.insert-more-data - if true, inserts 2 Users (ADMIN, USER) and provide some sample reservations

## How to run the project
Clone the repository, navigate to the project root directory 
and run the `./gradlew bootRun` or open project in IntelliJ and run it from there.

## How to test the project
Navigate to the project root directory and run the `./gradlew test` 
or open project in IntelliJ and run tests from there.

**WARNING:** If `app.insert-more-data=false`, all tests fail.

## Documentation
Use case, class and ERD diagrams can be found in `/docs` folder