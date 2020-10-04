package org.internship.system.mq;

import org.internship.system.models.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerMq {
    @Autowired
    private ConsumerManagement consumerManagement;

    @JmsListener(destination = "actions")
    public void consume(UserAction userAction){
        consumerManagement.addMessage(userAction);
    }
}
