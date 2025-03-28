# Orders Service

Orders Service is an application composed by a RESTful API and a Kafka consumer. The API exposes endpoints to retrieve orders data and the consumer receives new orders that will be processed and stored.
## Architecture

The following diagram shows in higher level of abstraction how the systems interact with each other:
`External Product A` publishes order requested events consumed by `Orders Service` to create new orders.
`External Product B` then can hit `Orders Service` Restful API endpoint to fetch information about the orders.
![](./docs/diagrams/system_context.png)

The following diagram shows how the systems interact with infrastructure components:
`External Product A` publishes messages to Kafka broker.
`External Product B` only interacts with `Orders Service` through HTTP requests.
`Orders Service` consumes messages from a Kafka broker, stores data in a MongoDB database and caches data in Redis cache.
![](./docs/diagrams/components.png)

## Starting the application

Please, make sure you have JDK 23. Setting up the Dockerfile is in the list of improvements.
```bash
./gradlew bootRun 
```

Make sure that docker-compose.yaml is running and then start all dependencies by running the following command:

```bash
docker compose up -d 
```

In order to create a new order, you can submit a order requested event by using the script in `scripts/kafka-message-producer/publish_order_request_event.sh`
The payload is also present in the same folder:

```json
{
    "customerUuid": "123e4567-e89b-12d3-a456-426614174000",
    "sellerUuid": "456e7890-e89b-12d3-a456-426614174111",
    "items": [
        {
            "productUuid": "789e0123-e89b-12d3-a456-426614174222",
            "quantity": 10,
            "price": 19.99
        }
    ]
}
```
You can see the created order information by submitting a request to the /v1/orders endpoint with the customer (uuid) or seller (uuid) as parameters, example:
```bash
curl -X GET "http://localhost:8080/v1/orders?customer=123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Or just use the swagger UI by accessing `http://localhost:8080/swagger-ui.html`

## Improvements
A few improvements that could be done both on API and event consumer:
* Add Avro as Kafka schema registry
* Separate the API and event consumer in two different independently deployable modules
  * This was made easier by leveraging DDD 
  * Also, the components packages were organized so that it would be easier to split later
* Create a Dockerfile to run the application without Java 23
* Add lint configuration