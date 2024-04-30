package com.seslab.bfsserver.configuration;

import com.seslab.bfsserver.handler.BrokerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicate;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BrokerEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(BrokerHandler handler) {
        return route(GET("/brokers/"), handler::all)
                .andRoute(GET("/brokers/{id}"), handler::getById)
                .andRoute(DELETE("/brokers/{id}"), handler::deleteById)
                .andRoute(POST("/brokers/"), handler::create)
                .andRoute(GET("/find").and(RequestPredicates.accept(MediaType.TEXT_HTML)), handler::findTheNearestBroker)
                .andRoute(PUT("/brokers/{id}"), handler::updateById);
    }
}
