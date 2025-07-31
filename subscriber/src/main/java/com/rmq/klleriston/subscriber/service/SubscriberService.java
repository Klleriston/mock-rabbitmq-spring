package com.rmq.klleriston.subscriber.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmq.klleriston.subscriber.model.QueueMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SubscriberService {

    private ObjectMapper objectMapper;

    @RabbitListener(containerFactory = "listenerContainerFactory", queues = "${rabbitmq-queueName}")
    public void receiveMessage(Message message) {
        String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        QueueMessage msgObject = (QueueMessage) jsonToObject(jsonMessage, QueueMessage.class);
        System.out.println(msgObject.toString());
    }

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }

    private Object jsonToObject(String jsonString, Class<?> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
