# Health Service

This project is to provide APIs for checking status of AGIM8 services. Server run at port: 9006

### Installation

Install the maven dependencies and start spring boot application.

```sh
$ cd health-service
$ mvn clean install
$ mvn spring-boot:run
```

### APIs

Below are requests to check status of the relevant services.

| Service | Method | URL |
| ------ | ------ | ------ |
| MongoDB | GET | /health/api/v1/mongodb
| Redis | GET | /health/api/v1/redis
| RabbitMQ | GET | /health/api/v1/rabbitmq

Response:
* [200 - OK] - Service available!
* [500 - INTERNAL SERVER ERROR] - Service is not available or having an problem with the health server.

### Code structure

* [application.yaml] - include information about configuration for each services.