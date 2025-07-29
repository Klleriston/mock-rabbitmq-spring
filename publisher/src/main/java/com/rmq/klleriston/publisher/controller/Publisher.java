package com.rmq.klleriston.publisher.controller;

import com.rmq.klleriston.publisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class Publisher {
    @Value("${rabbitmq-queueName}")
    private String queueName;

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/publish/text")
    public void publishText(@RequestBody String text) {
        System.out.println("Enviando mensagem: " + text);
        publisherService.publishTextMessage(text, queueName);
    }
}
