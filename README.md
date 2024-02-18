# BankAccount API

## Requirements

For building and running the application you need:

- [JDK 17](https://adoptium.net/temurin/releases/?os=any&arch=any&package=jdk&version=17)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.bank.bank_account.BankAccountApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Brief description

This API simulates a simple bank account api. You can view all accounts balance overview, withdraw and transfer money.

## Components

H2 Database is used for data storage in in-memory mode, in order to not have to have a separate database instance before 
application can be run

Swagger link once the API is running: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
You can test the API through swagger. Alternatively (since it's only a get endpoint at the moment)you can use postman 
there is a collection available in src/main/resources.

Controller advice is used for error handling.

OpenAPI uses the swagger file at src/main/resources/static.

## Assumptions
1. 1 account can have 1 iban and more than 1 payment cards. I was not very clear from the requirements if 1 account can 
only have 1 card or more. Ultimately I figured, it is more logical to be able to have more payment cards linked to an 
account
2. Only authentication included, no authorization. There is 1 overarching user (the client) that can call all apis. 
It currently uses a hard coded api token secret. This in reality would be fetched from a vault. 

## Future improvements
1. Paginate balances endpoint
2. Replace simple header authentication with complete Oauth2 authentication/authorization
3. Improve withdraw and transfer so that only users owning the account can do those actions. Fine grain balances 
endpoint so that tokens with certain access rights can use it.
4. Replace h2 with a database supporting advanced database features like PostgreSQL
5. Implement a migration library like Flyway or Liquibase
6. Implement monitoring and metric tools