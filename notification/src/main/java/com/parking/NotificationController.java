package com.parking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/notification")
public record NotificationController(NotificationService notificationService) {

    @GetMapping(path = "{customerId}")
    public NotificationMessage sendNotifs(@PathVariable("customerId") Integer customerId){
        String sendNotifs = notificationService.sendNotifsToCustomer(customerId);
        return new NotificationMessage(sendNotifs);
    }
}
