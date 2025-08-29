# support-ticket
Microservice with CRUD + Kafka Pub/Sub

Using in-memory DB (H2)

## Requirements
- Docker
- Java 17 ou 21
- Maven
- MacOS or Linux

## Coverage:
#### Source code in a GitHub repo:
``` 
git clone https://github.com/br11/support-ticket.git
``` 

## Coverage:

#### Commands:
``` 
cd support-ticket
mvn clean verify
```
#### Report location: 
```
./target/site/jacoco/index.html
```

## How to run tests.

### Commands:

#### On terminal #1: start the kafka container and the support ticket container
``` 
cd support-ticket

chmod +x ./tools/up
chmod +x ./tools/down

./tools/up
```

#### On terminal #2: start a kafka consumer to monitor ticket integration events (optional)
``` 
docker exec -it kafka bash
```
```
# Inside the container, run:
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic ticket-events \
  --from-beginning
```

#### On terminal #3: Invoke the support ticket API
``` 
# Create an open support ticket
curl -i -X POST http://localhost:8080/api/support-tickets \
  -H "Content-Type: application/json" \
  -d '{"title": "Test ticket", "description": "Test ticket description", "status": "OPEN"}'
```
<span style="color:yellow">*Replace {uuid} with the uuid of the support ticket created above*</span>
```
# Verify if the support ticket is OPEN
curl -i -X GET http://localhost:8080/api/support-tickets/{uuid}
```

```
# Update the support ticket’s status to IN_PROGRESS 
curl -i -X PUT http://localhost:8080/api/support-tickets/{uuid} \
  -H "Content-Type: application/json" \
  -d '{"title": "Test ticket", "description": "Test ticket description", "status": "IN_PROGRESS"}'
```

```
# Verify if the support ticket is IN_PROGRESS
> curl -i -X GET http://localhost:8080/api/support-tickets/{uuid} 
```

```
# Delete the support ticket
> curl -i -X DELETE http://localhost:8080/api/support-tickets/{uuid} 
```

```
# Verify if the support ticket was deleted by receiving a http 404 error
> curl -i -X GET http://localhost:8080/api/support-tickets/{uuid} 

```

#### On terminal #1: stop the kafka container and the support ticket container
``` 
./tools/down
```




