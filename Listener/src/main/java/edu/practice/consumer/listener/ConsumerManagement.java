package edu.practice.consumer.listener;

import edu.practice.consumer.database.MessagesTable;
import edu.practice.consumer.model.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerManagement {
    @Autowired
    private MessagesTable messagesTable;

    public void addMessage(UserAction userAction){
        messagesTable.addMessage(userAction.getLogin(),
                userAction.getAction(),
                userAction.getObject());
    }
}
