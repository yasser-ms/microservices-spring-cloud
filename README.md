# Spring Cloud Microservices Starter

A hands-on exploration of microservices architecture built from scratch. This project started as a learning journey into distributed systems and evolved into a fully functional service ecosystem with inter-service communication, service discovery, and load balancing.

---

## The Problem

Monolithic applications work until they don't. Scaling becomes painful, deployments become risky, and a single bug can bring down the entire system. This project explores how breaking an application into independent services solves these challenges.

---

## What I Built

A parking management system backend decomposed into independent services:

**Customer Service** handles user registration and management. Before accepting a new customer, it consults the Fraud Service to verify legitimacy.

**Fraud Service** maintains a registry of fraudulent actors and provides verification endpoints for other services.

**Eureka Server** acts as the central nervous system. Services register themselves on startup, and consumers discover providers by name rather than hardcoded addresses.

---

## Architecture

```
                         [Eureka Server :8761]
                                  |
                    +-------------+-------------+
                    |                           |
             [Customer :8080]            [Fraud :8081]
                    |                           |
                    +------ OpenFeign ----------+
                    |
             [PostgreSQL :5432]
```

When a customer registers:
1. Customer Service receives the request
2. Customer Service asks Fraud Service (via Feign): "Is this person a fraudster?"
3. Fraud Service checks its database and responds
4. Customer Service proceeds or rejects based on the response

No hardcoded URLs. If Fraud Service moves to a different port or if we spin up 10 instances, the system adapts automatically.

---

## Technical Decisions

**Why OpenFeign over RestTemplate?**
RestTemplate requires manual HTTP handling. Feign lets me define an interface, and the implementation is generated. Less boilerplate, fewer bugs.

**Why Eureka?**
In production, services scale up and down. IP addresses change. Eureka provides a single source of truth for service locations. Services register themselves; consumers ask Eureka where to find them.

**Why separate databases?**
Each service owns its data. Customer Service cannot directly query Fraud tables. This enforces boundaries and allows independent scaling and deployment.

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
cd customer && mvn spring-boot:run
```

**Verify:**
- Eureka Dashboard: http://localhost:8761
- Create customer: POST http://localhost:8080/api/v1/customers

---

## Project Structure

```
spring-cloud-microservices-starter/
├── customer/                 # Customer management service
├── fraud/                    # Fraud detection service
├── eureka-server/            # Service registry
├── docker-compose.yml        # PostgreSQL + pgAdmin
└── pom.xml                   # Parent POM with shared config
```

---

## Stack

- Java 17
- Spring Boot 3.2
- Spring Cloud (OpenFeign, Eureka)
- PostgreSQL
- Docker
- Maven (multi-module)

---

## What's Next

This project is preparation for Kubernetes deployment. The current Eureka-based discovery will be replaced by Kubernetes native service discovery, and the entire stack will run in containers orchestrated by K8s.

---

## Lessons Learned

Building this from scratch taught me more than any tutorial could. Understanding why services need discovery, experiencing firsthand what happens when you hardcode URLs, debugging Feign clients that silently fail. These are lessons that stick.

The code is not perfect. Some error handling is basic, some validations could be stricter. But it works, it scales, and most importantly, I understand every line of it.
