package com.seslab.bfsserver.mongorepository;

import com.seslab.bfsserver.model.Broker;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BrokerReactiveRepository extends ReactiveMongoRepository<Broker, String>{
    Flux<Broker> findByLocationWithin(Circle c);
}
