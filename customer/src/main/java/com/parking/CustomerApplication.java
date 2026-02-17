package com.parking;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients // With this annotation, we enable component scanning for interfaces that declare they are Feign clients.
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer, CustomerConfig customerConfig) {
        return args -> {
            producer.publish("foo", customerConfig.getInternalExchange(), customerConfig.getInternalNotificationRoutingKey());

        };
    };
}
