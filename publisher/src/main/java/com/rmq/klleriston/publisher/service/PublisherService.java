package com.rmq.klleriston.publisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import com.rmq.klleriston.publisher.model.QueueMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper;

    public void publishTextMessage(String message, String queueName) {
        System.out.println("Enviando mensagem -----> " + message);
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void publishJsonMessage(String message, String queueName) {
        System.out.println("Enviando mensagem JSON -----> " + message);
        QueueMessage msgObject = (QueueMessage) jsonToObject(message, QueueMessage.class);
        rabbitTemplate.convertAndSend(queueName, objectToJNode(msgObject));
    }

    private JsonNode objectToJNode(Object jsonObject) {
        return objectMapper.convertValue(jsonObject, JsonNode.class);
    }

    private Object jsonToObject(String jsonString, Class<?> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }
}
