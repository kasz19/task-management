# Oracle Challenge Backend

In this project we present de backend part for the oracle challenge. It is a Dropwizard project, and it 
exposes the endpoints required for the frontend application.

Please notice that there is one docker files in the root proyect:

Dockerfile -> Allows you to build a docker image from the current source files.

The current project consists in 5 classes:

- TaskManagementApp: The entry point of the application, loads the resource and the environment configuration
- TaskManagementAppConfig: Class that contains the database configuration of the application:
- TasksResource: Class that exposes the JAX RS Endpoints for the task microservice
- Task: DTO, that acts as well as JPA entity. It holds the description, the date and the task ID
- TaskDAO: Class that allows us to interact with the database

The unit tests are under the folder src/test/*.

About the database: We have chosen an hsqldb memory database. To modify this, generating a new file of 
configuration for the application should be enough (application-config.yaml). There is only ONE table, TASK, and the ID of this table is
expected to be auto-generated in the table (Identity Strategy)

In order to enable local testing, CORS requests have been enabled (So it is possible to invoke from the front-end to this application)

## Available Scripts

### `mvn clean install` to build the project and the launch the tests

### `java -jar target/task-crud-1.0-SNAPSHOT-shaded.jar server application-config.yaml` to run the project locally

### Docker Scripts

### `sudo docker build -f Dockerfile -t oracle-challenge-app .` to build the docker image

### `sudo docker run -p 3000:3000 oracle-challenge-frontend` to run the docker image for frontend project

### `sudo docker-compose up` to build both images (frontend + backend) and run them
