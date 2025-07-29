# Rabbit mq + Spring 

Repositorio destinado a compreensão de conceitos sobre filas e o uso de Rabbit MQ com Spring Boot

*Necessario rodar o comando docker para iniciar o container do rabbit junto de seu gerenciador*

```
docker run -d \
  --name my-rmq-container \         
  -p 8081:15672 \
  -p 5672:5672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin123 \
  rabbitmq:management
```
