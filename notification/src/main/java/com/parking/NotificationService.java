package com.parking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record NotificationService(
        NotificationRepository notificationRepository,
        JavaMailSender mailSender  // Add this
) {

    public NotificationMessage sendNotifsToCustomer(Integer customerId) {
        String message = "Hey customer, welcome back, you are not fraudulent";

        // Save to DB as before
        Notification notification = Notification.builder()
                .customerId(customerId)
                .message(message)
                .build();
        notificationRepository.save(notification);

        // Send email
        sendEmail(
                "yassermoussaoui004@gmail.com",  // Ideally fetch from CustomerRepository
                "Login test email",
                message
        );

        return new NotificationMessage(customerId, message);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(body);

        mailSender.send(email);
        log.info("Email sent to {}", to);
    }
}
