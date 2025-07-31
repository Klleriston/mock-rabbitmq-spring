package com.rmq.klleriston.subscriber.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory, RetryOperationsInterceptor retryOperationsInterceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryOperationsInterceptor);
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(cachingConnectionFactory());
    }

    @Bean
    Queue createQueue() {
        Queue q = QueueBuilder.durable(queueName).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    Queue boqQueue() {
        Queue q = QueueBuilder.durable("boq." + queueName).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    public RepublishMessageRecoverer  messageRecoverer(RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer republishMessageRecoverer = new RepublishMessageRecoverer(rabbitTemplate);
        republishMessageRecoverer.setErrorRoutingKeyPrefix("boq.");
        return republishMessageRecoverer;
    }

    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor(RepublishMessageRecoverer recoverer) {
        return RetryInterceptorBuilder.stateless().maxAttempts(2).backOffOptions(2000, 1, 100000).recoverer((MethodInvocationRecoverer<?>) recoverer).build();
    }
}
