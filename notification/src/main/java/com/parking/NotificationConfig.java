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
public class NotificationConfig {
    //Exchange
    @Value("$[rabbitmq.exchanges.internal")
    private String internalExchange;

    // Queue
    @Value("$[rabbitmq.queue.notification")
    private String notificationQueue;

    // The Binding
    @Value("$[rabbitmq.routing-keys.internal-notification")
    private String internalNotificationRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange(){
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue notificationQueue(){
        return new Queue(this.notificationQueue);
    }

    public Binding internalToNotificationBinding(){
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
