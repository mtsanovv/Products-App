# TechStore-App-Backend
A Java Spring REST app that has to maintain users and products.

For the frontend UI, please refer to [TechStore-App-Frontend](https://github.com/mtsanovv/TechStore-App-Frontend).

## Requirements
- Java 11 (this was created using OpenJDK 11.0.4)
- MySQL/MariaDB server (this was created using MariaDB 10.4.14)

## Running the app
1. Clone this repo.
2. Create a database using the given structure in the ```techstore.sql``` file in the ```sql``` directory. Create a user and grant them ```SELECT, INSERT, UPDATE, DELETE``` permissions **only** for the newly created database.
3. Open the file ```application.properties.dist```, located in the ```src/main/resources```directory.  
    - Configure the ```spring.datasource.url``` parameter. That is the connection URL for your MySQL database. You can learn more about that [here](https://www.javatpoint.com/example-to-connect-to-the-mysql-database) and [here](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html). Generally, if you have the MySQL/MariaDB server running on localhost:3306 and the newly created database is called ```techstore```, you may keep the preconfigured value for this parameter.
    - Configure the ```spring.datasource.username``` and ```spring.datasource.password``` parameters. Those are the credentials for the MySQL user you created in step 2. **Make sure it has the proper permissions for the correct database, as mentioned in step 2.**
    - Configure the ```http.allowed-origins``` parameter. Those are basically all the domains (with the protocol prefixes, http or https) that are allowed to access the REST API and they are separated by commas. An example has already been done. *It is extremely **insecure** to use http, whenever you are using this in production you should **always** use **https**.* Read more in the notes below, but for now, make sure to configure this parameter and don't leave it as-is.
    - Configure the ```spring.mail``` parameters. They are important for you to be able to receive email notifications when a product is depleted. You can learn more about that [here](https://www.baeldung.com/spring-email#2-spring-boot-mail-server-properties).
    - Configure the ```twitter.oauth``` parameters. They are used in order to allow merchants to post tweets on the TechStore Twitter account. You can learn more [here](https://www.baeldung.com/twitter4j) and on [the official Twitter4J page](http://twitter4j.org).
    - **The remaining parameters should remain unchanged.**
    - After you are done changing the configurations, rename ```application.properties.dist``` to ```application.properties```. **Note: all parameters should NEVER be left empty and should be configured accordingly.**
4. Have Gradle process the configuration and load all the dependencies. After it's done, run ```gradlew bootRun``` to run the Spring Boot app. 

## Building the app
- It's the same steps when running the app. However, instead of running ```gradlew bootRun```, you have to run ```gradlew build```. The executable jar is in inside ```build/libs``` after that. However, sometimes you might have to run ```gradlew clean``` before you do ```gradlew build```.
- After you build the executable jar, you can run it the standard way, by opening a terminal, going to the build/libs directory of the project and running ```java -jar techstore-X.X.X.jar```, where X.X.X is the version of your app, specified in the build.gradle configuration. **Make sure that you run the jar with Java 11 - to check which version you are using, run ```java -version``` before running the file.**

## Notes
- Always make sure the origins are https (unless it's localhost) - the basic HTTP authentication method used here should always be done over secure (https) connections.