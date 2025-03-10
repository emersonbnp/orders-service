# Orders Service

Orders Service is an application composed by a RESTful API and a Kafka consumer. The API exposes endpoints to retrieve orders data and the consumer receives new orders that will be processed and stored.

## Starting the application

Make sure that docker is running and then start all dependencies by running the following command:

```bash
docker compose up -d 
```
