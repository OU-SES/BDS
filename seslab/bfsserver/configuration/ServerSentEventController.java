package com.seslab.bfsserver.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seslab.bfsserver.event.BrokerCreatedEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ServerSentEventController {
    private final Flux<BrokerCreatedEvent> events;
    private final ObjectMapper objectMapper;

    public ServerSentEventController(BrokerCreatedEventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.events = Flux.create(eventPublisher).share();
        this.objectMapper = objectMapper;
    }

    @GetMapping(path="/sse/brokers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> brokers(){
        return this.events.map(produces-> {
            try {
                return objectMapper.writeValueAsString(produces);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
