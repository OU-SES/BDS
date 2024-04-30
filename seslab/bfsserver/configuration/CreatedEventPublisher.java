package com.seslab.bfsserver.configuration;

import com.seslab.bfsserver.event.BrokerCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class BrokerCreatedEventPublisher implements ApplicationListener<BrokerCreatedEvent>, Consumer<FluxSink<BrokerCreatedEvent>> {
    private final Executor executor;
    private final BlockingQueue<BrokerCreatedEvent> queue = new LinkedBlockingQueue<>();

    BrokerCreatedEventPublisher (Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(BrokerCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<BrokerCreatedEvent> sink) {
        this.executor.execute(() -> {
            while(true)
                try{
                    BrokerCreatedEvent event = queue.take();
                    sink.next(event);
                }catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
