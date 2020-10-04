package org.internship.system.mq;

import org.internship.system.db.MessagesTable;
import org.internship.system.db.OrganizationTables;
import org.internship.system.models.UserAction;
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
