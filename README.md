# REST API Articles

This repository includes a basic REST API built with Spring framework for recruitment process.

## Run and Test

To run the application type

```
mvn spring-boot:run
```

To execute unit and acceptance tests

```
mvn test
```

## Endpoints

**Important:** `Content-Type: application/json` header must be present to use API.

The most common HTTP status codes are returned when there is an error.

### Get articles

```
/articles [GET]
```

Get articles sorted by date descending.


### Get an article

```
/article/{id} [GET]
```

Gets an article with given id.


### Get articles by keyword

```
/articles/{keyword} [GET]
```

Get articles by keyword contains in article content and sorted by date descending.


### Add an article

```
/article [POST]

Content-Type: application/json

{
     "content": {
         "title": varchar(50),
         "text": longtext
     },
     "date": date("yyyy-mm-dd"),
     "magazine": {
        "id": int,
        "name": varchar(20)
     },
     "author": {
         "id": int,
         "firstName": varchar(10),
         "lastName": varchar(20)
     }
}
```

# Authors:
```
"author": {
    "id": 1,
    "firstName": "Adam",
    "lastName": "Smith"
}
"author": {
    "id": 2,
    "firstName": "John",
    "lastName": "Cook"
}
"author": {
    "id": 3,
    "firstName": "Jerry",
    "lastName": "Kowalsky"
}
```

# Authors:
```
"magazine": {
        "id": 1,
        "name": "Times"
     }
"magazine": {
        "id": 2,
        "name": "Pudelek"
     }
"magazine": {
        "id": 3,
        "name": "WP"
     }     
```

When succeed 201 Status code and newly created article object are returned.


### Update an article

```
/article [PUT]

Content-Type: application/json

{
     "content": {
         "title": varchar(50),
         "text": longtext
     },
     "date": date("yyyy-mm-dd"),
     "magazine": {
        "id": int,
        "name": varchar(20)
     },
     "author": {
         "id": int,
         "firstName": varchar(10),
         "lastName": varchar(20)
     }
}
```

When succeed 201 Status code and updated article object are returned.


### Delete an article

```
/article/{id} [DELETE]
```

Delete an article with given id.

### Database

You need to have **MySql** installed on your machine to run the API.

After creating the API database, you need to add your **MySql** root `username` and `password` in the `application.properties` file on `src/main/resource` and `url` to empty database schema. The lines that must be modified are as follows:

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```
