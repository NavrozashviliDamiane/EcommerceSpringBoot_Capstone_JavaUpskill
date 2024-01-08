# E-commerce Microservices Architecture Capstone Project Epam Upskill 

Welcome to the E-commerce Microservice Architecture project! This project demonstrates building multiple 
microservices that communicate with each other. It shows various modules and services, including configuration server, 
Eureka server, API Gateway, Product Service, Order Service, Account Service and databases. 

## Introduction

This project contains multiple modules:

- **Config server**: Manages service configuration information using a file system or GitHub-based repository.
- **Service Registry**: Allows multiple service instances to register and lookup the physical location of target services.
- **Cloud Gateway**: Routes microservices and allows JWT authentication logic (with connection to account service).
- **Product Service**: Handles Product related functionalities.
- **Order Service**: Manages business logic related to order placement.
- **Account Service**: Manages user Authentication and Authorisation.


## Initial Configuration

Make sure you have the following prerequisites installed:

- Apache Maven (http://maven.apache.org)
- Git Client (http://git-scm.com)
- Docker (https://www.docker.com/products/docker-desktop)
- Java 17

## How To Use

Clone this repository and navigate to the project directory:

```bash
$ git clone https://github.com/NavrozashviliDamiane/EcommerceSpringBoot_Capstone_JavaUpskill.git
$ cd microservice-architecture-demo


#Before building docker images and run, we need to edit inside docker-compose.yml file google cloud key.json file path
based on the local location.

1. Open docker-compose.yml file and navigate to product-service section: 
product-service:
    build:
      context: ./product-service
    ports:
      - 8082:8082
    environment:
      - SERVICE_ACCOUNT_FILE_PATH=/app/resources/key.json
    volumes:
      - /C/Users/Admin/Desktop/CapstoneProject/JavaPage_Ecommerce/product-service/src/main/resources:/app/resources
2. In volumes field change write absolut path of the key.json file based on your local location. 


# Now we are going to use docker-compose to build images and run as containers.  
stay in main directory whre docker-compose.yml file is located and run command
$ docker-compose -f docker/docker-compose.yml up --build

```




## Contact

Please feel free to contact regarding any issue navrozashvili1997@gmail.com. 
