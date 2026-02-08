package com.parking;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "notification")
public interface NotificationClient {
    @GetMapping("/api/v1/notification/{customerId}")
    NotificationMessage sendNotifs(@PathVariable("customerId") Integer customerId);
}
