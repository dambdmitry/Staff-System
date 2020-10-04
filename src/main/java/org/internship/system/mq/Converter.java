package org.internship.system.mq;

import org.internship.system.models.UserAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.Collections;

@Configuration
public class Converter {
    @Bean
    public MappingJackson2MessageConverter messageConverter(){
        final MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("content-type");
        messageConverter.setTypeIdMappings(Collections.singletonMap("action", UserAction.class));
        return messageConverter;
    }
}
