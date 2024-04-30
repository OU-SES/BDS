package com.seslab.bfsserver.event;

import com.seslab.bfsserver.model.Broker;
import org.springframework.context.ApplicationEvent;

public class BrokerCreatedEvent extends ApplicationEvent {
    public BrokerCreatedEvent(Broker broker) {
        super(broker);
    }
}
