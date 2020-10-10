package edu.practice.consumer.listener;

import edu.practice.consumer.database.MessagesTable;
import edu.prctice.staff.models.UserAction;
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
