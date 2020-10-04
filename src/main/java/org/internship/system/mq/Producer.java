package org.internship.system.mq;

import org.internship.system.models.ModelFactory;
import org.internship.system.models.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    @Autowired
    private JmsTemplate template;

    public void sendMessage(String action, String object){
        UserAction userAction = ModelFactory.createUserAction(getLogin(), action, object);
        template.convertAndSend("actions", userAction);
    }

    private String getLogin(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
