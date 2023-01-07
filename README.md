# Comprehensive Curriculum Design


> ### Spring web + hibernate + Spring Session + Springdoc ( CRUD, Auth, Openapi, Swagger-UI, etc ). 
This is my undergraduate comprehensive curriculum design, which is a simple content management system backend RESTful API .

## 1. Introduction
- Although the design requirements of my undergraduate comprehensive courses are simple, I still try my best to split and abstract the only business logic.
- The comments in the project code are enough to be read as documentation. In readme, I will introduce the project composition as succinctly as possible.

## 2. Project structure
### 2.1. database
- [Database package](https://github.com/panghaidong/CurriculumDesignBackend/tree/master/src/main/java/com/example/application/database) abstracts the database operation, and entity.
- The project uses MySQL as the database, and dependence on hibernate as an orm tool to map database schema.
- In database schema, there are only two business tables: user and article. Of course, this project uses jdbc for http session persistence. Spring-Session create two tables: spring_session and spring_session_attributes to store sessions.
### 2.2. controllers
- [Controllers package](https://github.com/panghaidong/CurriculumDesignBackend/tree/master/src/main/java/com/example/application/apis) is the core of the project. It contains all the business logic.
- Read code comments to understand the business logic.
### 2.3. schemas
- [Schemas package](https://github.com/panghaidong/CurriculumDesignBackend/tree/master/src/main/java/com/example/application/schemas) , it is used as a constraint for the api to pass in and return data.
- It is also used to generate the openapi document.
## 3. Project configuration
[Configuration file](https://github.com/panghaidong/CurriculumDesignBackend/tree/master/src/main/resources/application.properties)
## 4. Project development
pass