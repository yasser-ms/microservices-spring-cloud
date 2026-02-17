package com.parking.notificationListener;
import com.parking.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;
    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(Integer customerId){
        log.info("Consumed {} from queue",customerId);
        notificationService.sendNotifsToCustomer(customerId);
    }
}
