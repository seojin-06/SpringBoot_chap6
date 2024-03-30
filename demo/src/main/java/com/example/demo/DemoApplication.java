package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@EnableScheduling
@Component
class PlaneFinderPoller {
	private WebClient client = WebClient.create("http://localhost:7634/aircraft");

	private final RedisConnectionFactory connectionFactory;
	private final AircraftRepository repository;

	PlaneFinderPoller(RedisConnectionFactory connectionFactory,
					  AircraftRepository repository) {
		this.connectionFactory = connectionFactory;
		this.repository = repository;
	}

	@Scheduled(fixedRate = 1000) //폴링 빈도 결정
	private void pollPlanes() {
		connectionFactory.getConnection().serverCommands().flushDb();

		client.get()
				.retrieve()
				.bodyToFlux(Aircraft.class)
				.filter(plane -> !plane.getReg().isEmpty())
				.toStream()
				.forEach(repository::save);

		repository.findAll().forEach(System.out::println);
	}
}

