package com;

import com.seslab.bfsserver.model.Broker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.seslab.bfsserver.mongorepository.BrokerReactiveRepository;

import org.springframework.data.annotation.Id;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class BfSserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BfSserverApplication.class, args);
	}

	@Bean
	ApplicationRunner init(BrokerReactiveRepository repository) {
	  //dummy broker information for test
	  Object[][] data = {
	      {"Broker1", "127.0.0.1", "1", 0, 4, 1.5f, 100.0f, -20.0f, 40.757271, -73.990000},
	      {"Broker2", "127.0.0.2", "2", 1, 4, 1.5f, 100.0f, -20.0f, 40.757218, -73.990007},
	      {"Broker3", "127.0.0.3", "3", 2, 4, 1.5f, 100.0f, -20.0f, 40.757104, -73.989724},
		  {"Broker4", "127.0.0.3", "4", 3, 4, 1.5f, 100.0f, -20.0f, 4124.9170, 08151.6811}
	  };
	  //dummy broker GPS data
	  double[][] location = {
		  {40.757396,  -73.989859,},
		  {40.757218,  -73.990007,},
		  {40.757104,  -73.989724,},
		  {4124.9170,  08151.6811,}
	  };

	  return args -> {
	      repository
	          .deleteAll()
	          .thenMany(
	              Flux
	                  .just(data)
	                  .map(array -> {
	                  		double[] temp_loc = {(Double) array[8], (Double) array[9]};
	                      return new Broker((String) array[0], (String) array[1], (String) array[2], (Integer) array[3], 1, (Integer) array[4], (Float) array[5], (Float) array[6], (Float) array[7], temp_loc);
	                  })
	                  .flatMap(repository::save)
	          )
	          .thenMany(repository.findAll())
	          .subscribe(kayak -> System.out.println("saving " + kayak.toString()));
	  };
	}

}
