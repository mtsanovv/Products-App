# TechStore-App-Backend
A Java Spring REST app that has to maintain users and products.

For the frontend UI, please refer to [TechStore-App-Frontend](https://github.com/mtsanovv/TechStore-App-Frontend).

## Requirements
- Java 11 (this was created using OpenJDK 11.0.4)
- MySQL/MariaDB server (this was created using MariaDB 10.4.14)

## Running the app
1. Clone this repo.
2. Create database using the given structure in the ```techstore.sql``` file in the ```sql``` directory.
3. Open the file ```application.properties.dist```, located in the ```src/main/resources```directory.  Configure the SQL database name and user access (that user should only have SELECT, INSERT, UPDATE, DELETE permissions on the TechStore database, and nowhere else). Configure the allowed origins (an example of how they should be written is already done - domains with http:// or https:// prefixes, separated by commas). Don't forget to configure the mailing settings as well or you may not be able to receive mail notifications for depleted products. After you are done changing the configurations, rename ```application.properties.dist``` to ```application.properties```. **Note: all parameters should NEVER be left empty and should be configured accordingly.**
4. Have Gradle download the dependencies. After it's done, run ```gradlew bootRun``` to run the Spring Boot app. 

## Notes
- Always make sure the origins are https (unless it's localhost) - the basic HTTP authentication method used here should always be done over secure (https) connections;
- CSRF is disabled: this project was not made to be public. In order for CSRF to be implemented properly, this all should've been generated by Thymeleaf in Spring.