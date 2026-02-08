package com.parking;


import org.springframework.stereotype.Service;

@Service
public record NotificationService(NotificationRepository notificationRepository) {
    public String sendNotifsToCustomer(Integer customerId){
        Notification notification = Notification.builder().
                customerId(customerId).message("Hey customer, welcome back, you are not fraudulent").
                build();
        notificationRepository.save(notification);
        return "Hey customer, welcome back, you are not fraudulent";
    }
}
