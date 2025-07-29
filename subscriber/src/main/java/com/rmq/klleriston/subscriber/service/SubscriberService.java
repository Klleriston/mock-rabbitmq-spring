package com.rmq.klleriston.subscriber.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

    @RabbitListener(queues = "${rabbitmq-queueName}")
    public void receiveMessage(Message message) {
        System.out.println(message);
    }
}
