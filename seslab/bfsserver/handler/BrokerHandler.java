package com.seslab.bfsserver.handler;

import com.seslab.bfsserver.BrokerService.BrokerService;
import com.seslab.bfsserver.model.Broker;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.net.URI;

@Component
public class BrokerHandler {
    private final BrokerService brokerService;

    public BrokerHandler(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    public Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.brokerService.get(id(r)));
    }

    public Mono<ServerResponse> all(ServerRequest r ) {
        return defaultReadResponse(this.brokerService.all());
    }

    public Mono<ServerResponse> deleteById(ServerRequest r ) {
        return defaultReadResponse(this.brokerService.delete(id(r)));
    }

    public Mono<ServerResponse> findTheNearestBroker(ServerRequest r) {
        String lat = r.queryParam("lat").get();
        String lng = r.queryParam("lng").get();
        //System.out.println("finding broker....lat:"+lat+", lng:"+lng);

        return defaultReadResponse(this.brokerService.searchNearBroker(lat,lng));
    }

    public Mono<ServerResponse> updateById(ServerRequest r) {
        Flux<Broker> id = r.bodyToFlux(Broker.class)
                .flatMap(b -> this.brokerService.update(id(r), b.getCntClient()));
        return defaultReadResponse(id);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<Broker> flux = request
                .bodyToFlux(Broker.class)
                .flatMap(b -> this.brokerService.create(b.getId(), b.getIpAddress(),b.getPort(), b.getNumSat(), b.getHDOP(), b.getAltitude(), b.getHeight(), b.getLocation()));
        return defaultWriteResponse(flux);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Broker> brokers){
        return Mono
                .from(brokers)
                .flatMap(b -> ServerResponse
                    .created(URI.create("/brokers/"+b.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .build()
                );
    }

    public static Mono<ServerResponse> defaultReadResponse(Publisher<Broker> brokers) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(brokers, Broker.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }

    private static String lat(ServerRequest r) {
        return r.pathVariable("lat");
    }

    private static String lng(ServerRequest r) {
        return r.pathVariable("lng");
    }
}
