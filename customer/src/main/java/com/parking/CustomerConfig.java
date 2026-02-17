package com.parking;

import jakarta.validation.Valid;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfig {
    //Exchange
    @Value("${rabbitmq.exchanges.internal}")  // Fixed all three: ${}, closing brace, correct key
    private String internalExchange;

    // Queue
    @Value("${rabbitmq.queues.notification}")  // Fixed
    private String notificationQueue;

    // The Binding
    @Value("${rabbitmq.routing-keys.internal-notification}")  // Fixed
    private String internalNotificationRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange(){
        System.out.println("Creating exchange: " + this.internalExchange);  // Debug
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue notificationQueue(){
        System.out.println("Creating queue: " + this.notificationQueue);  // Debug
        return new Queue(this.notificationQueue);
    }

    @Bean
    public Binding internalToNotificationBinding(){
        System.out.println("Creating binding: " + this.internalNotificationRoutingKey);  // Debug
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(this.internalNotificationRoutingKey);
    }

    public String getNotificationQueue() {
        return notificationQueue;
    }

    public String getInternalNotificationRoutingKey() {
        return internalNotificationRoutingKey;
    }

    public String getInternalExchange() {
        return internalExchange;
    }
}
