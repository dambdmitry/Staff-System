package edu.practice.consumer.listener;

import edu.prctice.staff.models.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @Autowired
    private ConsumerManagement consumerManagement;

    @JmsListener(destination = "actions")
    public void consume(UserAction userAction){
        consumerManagement.addMessage(userAction);
    }
}

