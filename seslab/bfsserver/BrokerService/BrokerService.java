package com.seslab.bfsserver.BrokerService;

import com.seslab.bfsserver.event.BrokerCreatedEvent;
import com.seslab.bfsserver.model.Broker;
import com.seslab.bfsserver.mongorepository.BrokerReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class BrokerService {
    private final ApplicationEventPublisher publisher;
    private final BrokerReactiveRepository repository;

    public BrokerService(ApplicationEventPublisher publisher, BrokerReactiveRepository repository) {
        this.publisher = publisher;
        this.repository = repository;

    }

    public Flux<Broker> all() {
        return this.repository.findAll();
    }

    public Mono<Broker> get(String id) {
        return this.repository.findById(id);
    }

    public Flux<Broker> searchNearBroker(String lat,String lng) {
        return this.repository.findByLocationWithin(new Circle(Double.parseDouble(lat), Double.parseDouble(lng), 0.0001));
    }

    public Mono<Broker> update(String id, int cnt) {

        return this.repository
                .findById(id)
                .map(broker -> new Broker (
                        broker.getId(),
                        broker.getIpAddress(),
                        broker.getPort(),
                        cnt,
                        1,
                        broker.getNumSat(),
                        broker.getHDOP(),
                        broker.getAltitude(),
                        broker.getHeight(),
                        broker.getLocation()
                        ))
                .flatMap(this.repository::save);
    }

    public Mono<Broker> delete(String id) {
        return this.repository
                .findById(id)
                .flatMap(broker -> this.repository.deleteById(broker.getId()).thenReturn(broker));
    }

    public Mono<Broker> create(String id, String ip,String port, int numberOfSat, float HDOP, float altitude, float height, double[] location) {
        System.out.println("saving Broker(id="+id+", ipAddress="+ip+", port="+port+
                ", cntClient=0, fixQuality=1, numSat="+numberOfSat+", HDOP="+HDOP+", altitude="+altitude+", height="+height+
                ", location["+location[0]+""+location[1]+"])");
        return this.repository
                .save(new Broker(id, ip, port, 0, 1, numberOfSat, HDOP, altitude, height, location))
                .doOnSuccess(broker -> this.publisher.publishEvent(new BrokerCreatedEvent(broker)));
    }
}
