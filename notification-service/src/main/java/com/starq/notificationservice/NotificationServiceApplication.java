package com.starq.notificationservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(NotificationServiceApplication.class, args);

    }
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
    // send out an email notification
      log.info("recieved Notification for order - {}", orderPlacedEvent.getOrderNumber());
    }
}
