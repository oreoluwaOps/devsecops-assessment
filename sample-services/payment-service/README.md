# DevSecOps Sample Microservices – Java Spring Boot

This repository contains three **minimal** Java Spring Boot microservices
('account-service', 'payment-service' and 'transaction-service') that mirror the
sample services described in the Peerless DevSecOps assessment.  The goal
is to provide a broken baseline: each service includes intentional
security and deployment flaws. Candidates are
expected to identify and remediate these issues as part of the assessment.

## Services Overview

| Service | Port | Description |
|---------|------|-------------|
| **account-service** | 8081 | Maintains account balances.  Provides a `GET /balance` and `POST /credit` endpoint.|
| **payment-service** | 8080 | Processes payments by calling the account service. |
| **transaction-service** | 8082 |

All three services are packaged as standalone Spring Boot applications with
their own Maven 'pom.xml', 'Dockerfile' and Kubernetes manifest.

## Running Locally

1. Build the service using Maven:

   ```bash
   cd account-service
   mvn package
   java -jar target/account-service-0.0.1-SNAPSHOT.jar
   ```

2. Repeat for the other services, adjusting the port numbers.

Endpoints can be accessed at 'http://localhost:<port>/'.

## Docker

Each service includes a simple 'Dockerfile'.  These files intentionally
demonstrate bad practices.

To build and run the account service:

```bash
cd account-service
mvn package  # produces target/account-service-0.0.1-SNAPSHOT.jar
docker build -t account-service:latest .
docker run -p 8081:8081 account-service:latest
```

## Kubernetes

Each service has a corresponding 'k8s.yaml' which defines a basic
'Deployment' and 'Service'.  

Deploy the account service via:

```bash
kubectl apply -f account-service/k8s.yaml
```

Repeat for the other services.  Note that you must build and push the
container images to a registry accessible by your cluster ('payment-service:latest'
in the manifest is a placeholder).

## Purpose and Caveats

These services are intentionally flawed to illustrate common DevSecOps
pitfalls.  They should **never** be used in production.  Feel free to
extend, restructure or secure them as part of your assessment.  Be sure to
document any changes and the rationale behind them.
