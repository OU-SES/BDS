package com.seslab.bfsserver.configuration;

import com.seslab.bfsserver.BrokerService.BrokerService;
import com.seslab.bfsserver.model.Broker;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(path = "/brokers", produces = MediaType.APPLICATION_JSON_VALUE)
@org.springframework.context.annotation.Profile("brokerTest")
public class BrokerController {

	private final BrokerService brokerService;
	private final MediaType mediaType = MediaType.APPLICATION_JSON;

	public BrokerController(BrokerService brokerService) {
		this.brokerService = brokerService;
	}

	@GetMapping
	Publisher<Broker> getAll() {
		return this.brokerService.all();
	}

	@GetMapping("/{id}")
	Publisher<Broker> getById(@PathVariable("id") String id) {
		return this.brokerService.get(id);
	}

	@PostMapping
	Publisher<ResponseEntity<Broker>> create(@RequestBody Broker broker) {
		return this.brokerService
				.create(broker.getId(), broker.getIpAddress(),broker.getPort(), broker.getNumSat(), broker.getHDOP(), broker.getAltitude(), broker.getHeight(), broker.getLocation())
				.map(b -> ResponseEntity.created(URI.create("/brokers/" + b.getId()))
						.contentType(mediaType)
						.build());
	}

	@DeleteMapping("/{id}")
	Publisher<Broker> deleteById(@PathVariable String id) {
		return this.brokerService.delete(id);
	}

	@PutMapping("/{id}")
	Publisher<ResponseEntity<Broker>> updateById(@PathVariable String id, @RequestBody Broker broker) {
		return Mono
				.just(broker)
				.flatMap(b -> this.brokerService.update(id, b.getCntClient()))
				.map(b -> ResponseEntity
					.ok()
					.contentType(this.mediaType)
					.build());
	}
}
