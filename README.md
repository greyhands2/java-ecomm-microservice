# java-ecomm-microservice : Springboot, Netflix Eureka, OAuth2, Keycloak, Zipkin, Kafka, Zookeeper, Postgres, Mongodb, Resilience4j, Macrometer Tracing, Prometheus, Grafana, Docker, Kubernetes



## <H2>In order to use the jib dockerfile alternative you have to add it's maven plugin to your parent project pom xml file and also edit your settings.xml file and add your dockerhub credentials , then run the command mvn clean compile jib:build. This would install all the images on your dockerhub account making it possible for our docker-compose.yml file to pull them for installation on your local docker machine

## Also edit your system's host file at /etc/hosts and redirect traffic from keycloak to localhost. Do this: "127.0.0.1 keycloak"

## add that line in your host file
