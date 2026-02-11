# Spring Cloud Microservices 

A hands-on exploration of microservices architecture built from scratch. This project started as a learning journey into distributed systems and evolved into a fully functional service ecosystem with inter-service communication, service discovery, load balancing, and distributed tracing.

## The final architecture 
<img width="1495" height="882" alt="Screenshot From 2026-02-04 21-22-22" src="https://github.com/user-attachments/assets/32506131-ef23-49f6-a324-60ad31e0d654" />

---

## The Problem

Monolithic applications work until they don't. Scaling becomes painful, deployments become risky, and a single bug can bring down the entire system. This project explores how breaking an application into independent services solves these challenges.

---

## What I Built

**Customer Service** handles user registration and management. Before accepting a new customer, it consults the Fraud Service to verify legitimacy, then triggers a notification.

**Fraud Service** maintains a registry of fraudulent actors and provides verification endpoints for other services.

**Notification Service** sends welcome messages to newly registered customers after fraud verification passes.

**Eureka Server** acts as the central nervous system. Services register themselves on startup, and consumers discover providers by name rather than hardcoded addresses.

**Distributed Tracing** with Micrometer and Zipkin allows tracking a single request as it flows through all services.

---

## Architecture

```
                         [Eureka Server :8761]
                                  |
                          [APIGateway :8083] 
          +-----------------------+-----------------------+
          |                       |                       |
   [Customer :8080]        [Fraud :8081]        [Notification :8082]
          |                       |                       |
          +----- OpenFeign -------+----- OpenFeign -------+
          |
   [PostgreSQL :5432]
          
                         [Zipkin :9411]
                              |
          +-------------------+-------------------+------------------+
          |                   |                   |                  |
      customer             fraud            notification         API Gateway
      (traces)            (traces)            (traces)            (tarces)     
```

When a customer registers:
1. Customer Service receives the request
2. Customer Service asks Fraud Service (via Feign): "Is this person a fraudster?"
3. Fraud Service checks its database and responds
4. If not a fraudster, Customer Service calls Notification Service
5. Notification Service sends a welcome message
6. All steps are traced with the same Trace ID in Zipkin

No hardcoded URLs. If Fraud Service moves to a different port or if we spin up 10 instances, the system adapts automatically.

---

## Technical Decisions

**Why OpenFeign over RestTemplate?**
RestTemplate requires manual HTTP handling. Feign lets me define an interface, and the implementation is generated. Less boilerplate, fewer bugs.

**Why Eureka?**
In production, services scale up and down. IP addresses change. Eureka provides a single source of truth for service locations. Services register themselves; consumers ask Eureka where to find them.

**Why separate databases?**
Each service owns its data. Customer Service cannot directly query Fraud tables. This enforces boundaries and allows independent scaling and deployment.

**Why Micrometer Tracing over Sleuth?**
Spring Cloud Sleuth is deprecated in Spring Boot 3.x. Micrometer Tracing is the modern replacement, providing the same functionality with better integration.

**Why Zipkin?**
When a request fails across multiple services, finding the root cause is painful. Zipkin visualizes the entire request flow, showing exactly where time is spent and where failures occur.

**Note on current setup:** For local development, services share a single PostgreSQL instance with separate databases. In production, each service would have its own database server.

---

## API Reference

### Customer Service (port 8080)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/customers | Register new customer |
| GET | /api/v1/customers | List all customers |
| GET | /api/v1/customers/{id} | Get customer by ID |
| PUT | /api/v1/customers/{id} | Update customer |
| DELETE | /api/v1/customers/{id} | Delete customer |
| GET | /api/v1/customers/search?email={email} | Search by email |

### Fraud Service (port 8081)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/fraud/{customerId} | Check if customer is fraudster |

### Notification Service (port 8082)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/notification/{customerId} | Send notification to customer |

---

## Running Locally

**Prerequisites:**
- Java 17+
- Maven
- Docker

**Start infrastructure:**
```bash
docker-compose up -d
```

**Start services (in order):**
```bash
# Terminal 1 - Eureka must start first
cd eureka-server && mvn spring-boot:run

# Terminal 2
cd fraud && mvn spring-boot:run

# Terminal 3
cd notification && mvn spring-boot:run

# Terminal 4
cd customer && mvn spring-boot:run
```

**Verify:**
- Eureka Dashboard: http://localhost:8761
- Zipkin Dashboard: http://localhost:9411
- Create customer: POST http://localhost:8080/api/v1/customers

---

## Project Structure

```
spring-cloud-microservices-starter/
├── customer/                 # Customer management service
├── fraud/                    # Fraud detection service
├── notification/             # Notification service
├── eureka-server/            # Service registry
├── docker-compose.yml        # PostgreSQL, pgAdmin, Zipkin
└── pom.xml                   # Parent POM with shared config
```

---

## Stack

- Java 17
- Spring Boot 3.2
- Spring Cloud (OpenFeign, Eureka)
- Micrometer Tracing with Brave
- Zipkin
- PostgreSQL
- Docker
- Maven (multi-module)

---

## Distributed Tracing

Every request gets a unique Trace ID that follows it through all services:

```
Customer logs: INFO [customer,abc123,def456] Processing request
Fraud logs:    INFO [fraud,abc123,ghi789] Checking customer
Notification:  INFO [notification,abc123,jkl012] Sending message
```

Same Trace ID (abc123) = same request. Zipkin aggregates these into a visual timeline showing the complete request flow.

## API Gateway : 

Every request should by an API Gateway ( load balancer ), to get him to the right route he needed

Note : In production, there are ressources that handles lead balacing very well, to actually focus on the project him self ( Services ) and not trying to configure load balancer from scratch 
some ressources : 
  google cloud load balancer : https://cloud.google.com/load-balancing
  AWS Elastic Load Balancing : https://aws.amazon.com/fr/elasticloadbalancing/
  Nginx load balancing : https://docs.nginx.com/nginx/admin-guide/load-balancer/http-load-balancer/
  
```
spring:
  application:
    name: apiGW
  zipkin:
    base-url : http://localhost:9411
  cloud:
    gateway:
      routes:
        - id:customer
        - uri : lb://CUSTOMER
          predicates:
            - Path = /api/v1/customer/*
        - id:fraud
        - uri: lb://FRAUD
          predicates:
            - Path = /api/v1/fraud/*
        - id:notification
        - uri: lb://NOTIFICATION
          predicates:
            - Path = /api/v1/notification/*
```

---

## What's Next

This project is preparation for Kubernetes deployment. The current Eureka-based discovery will be replaced by Kubernetes native service discovery, and the entire stack will run in containers orchestrated by K8s.

Upcoming additions:
- RabbitMQ for async messaging
- Kubernetes deployment
---

## Lessons Learned

Building this from scratch taught me more than any tutorial could. Understanding why services need discovery, experiencing firsthand what happens when you hardcode URLs, debugging Feign clients that silently fail, figuring out why trace IDs weren't propagating between services. These are lessons that stick.

The code is not perfect. Some error handling is basic, some validations could be stricter. But it works,and it scales.
