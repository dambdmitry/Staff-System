package org.internship.system.kafka;

import org.internship.system.config.ApplicationConfig;
import org.internship.system.models.ModelFactory;
import org.internship.system.models.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ProducerManagement {
    @Autowired
    private KafkaTemplate<String, UserAction> kafkaTemplate;
    private static final String TOPIC = ApplicationConfig.getProperty(ApplicationConfig.KAFKA_TOPIC);

    public void sendMessage(String action, String object){
        UserAction userAction = ModelFactory.createUserAction(getLogin(), action, object);
        kafkaTemplate.send(TOPIC, userAction);
    }

    private String getLogin(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
