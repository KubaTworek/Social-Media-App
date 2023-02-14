# Articles App
> Full-stack application presenting standard CRUD capabilities to the user. The main goal of the application is to manage a database of articles created by different people and coming from different sources.


## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Run and Test](#run-and-test)
* [Endpoints](#endpoints)
* [Database](#database)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Contact](#contact)


## General Information
- Front-end of the application has been written in vanilla JavaScript in architecture based on Web Components.


- The backend of the application was written based on microservice architecture. Every front-end service written in Java was reformatted to Kotlin language. Spring Boot framework was used to create the project. The database is temporarily based on an embedded H2 database.


- All microservices run in a Docker container.


- The purpose of writing this project is to improve my programming skills, learn various tools and demonstrate my ability to write readable and working code.


- Initially, the application was a simple REST API written in Java for the recruitment process. It went through a transformation caused by my desire to learn the Kotlin language, microservices architecture and build a more complex frontend. The current version is a complete full-stack application, which I will continue to use to learn new tools and correct programming practices.


## Technologies Used
- Kotlin
- Spring Boot
- Spring Cloud
- Hibernate, JPA

## Run and Test

First we need to create images for every microservice

```
mvn spring-boot:build-image
```

To create a container

```
docker-compose up
```

## Endpoints

This API provides HTTP endpoint's and tools for the following:

### Frontend
url = http://localhost:8881/

Articles Page = http://localhost:8881/#/articles

Authors Page = http://localhost:8881/#/authors

Magazines Page = http://localhost:8881/#/magazines

### Article
port = 2111
* Create an article: `POST/api/articles/`
* Delete an article (by id): `DELETE/api/articles/{articleId}`
* Find article (by id): `DELETE/api/articles/{articleId}`
* Find all articles: `GET/api/articles`
* Find all articles by keyword: `GET/api/articles/{keyword}`


Request Body [Article]
```
{
     "title": varchar(40),
     "text": varchar(255),
     "authorId": Integer
     "magzineId": Integer
}
```

### Author
port = 2112
* Create an author: `POST/api/authors/`
* Delete an author (by id): `DELETE/api/authors/{articleId}`
* Find author (by id): `DELETE/api/authors/{authorId}`
* Find all authors: `GET/api/authors`
* Find all authors by keyword: `GET/api/authors/{keyword}`


Request Body [Author]
```
{
     "firstName": varchar(50),
     "lastName": varchar(50)
}
```

### Magazine
port = 2113
* Create an magazine: `POST/api/magazines/`
* Delete an magazine (by id): `DELETE/api/magazines/{articleId}`
* Find magazine (by id): `DELETE/api/magazines/{magazineId}`
* Find all magazines: `GET/api/magazines`
* Find all magazines by keyword: `GET/api/magazines/{keyword}`



Request Body [Magazine]
```
{
     "magazineName": varchar(50),
     "test": "test"
}
```

## Database

The database is distributed. Each microservice has its own database describing one key part of the whole application.

### Schema
![Database schema](database/schema.png)

### Description ENG

The database represents a standardized schema for the "Articles" apllication.

Articles can be written by one author and published in one magazine. Each has also an entity with the content of the entire article.
To add an article, there must already be an author and a magazine to which we can assign it.

Deletion of an author or magazine deletes all articles belonging to it. On the other hand, deleting an article has no effect on the authors or magazines.


## Project Status
Project is: _in_progress_


## Room for Improvement

Room for improvement:
- Make frontend more fancy 
- Add frontend based on Svelte
- Add frontend based on React
- Add Security microservice to authenticate users
- Make Unit Test
- Make Integration Tests
- Improve microservice architecture
- Add Elastic Stack
- Prepare schema of database


## Contact
Created by [Jakub Tworek](https://github.com/KubaTworek)
