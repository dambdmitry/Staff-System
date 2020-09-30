package org.internship.system.kafka;

import org.internship.system.config.ApplicationConfig;
import org.internship.system.models.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @Autowired
    private ConsumerManagement consumerManagement;
    private final String TOPIC = ApplicationConfig.getProperty(ApplicationConfig.KAFKA_TOPIC);
    @KafkaListener(topics = "Actions", groupId = "action", containerFactory = "kafkaListenerContainerFactory")
    public void consume(UserAction userAction){
        consumerManagement.addMessage(userAction);
    }
}
