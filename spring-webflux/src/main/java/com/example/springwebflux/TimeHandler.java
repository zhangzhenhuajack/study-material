package com.example.springwebflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TimeHandler {
	public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
		return ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
	}

	public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
		return ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
	}

	public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {

		StringBuilder result = new StringBuilder("abc");
		Thread thread = new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					result.append(i);
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();

		return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(  // 1
				Flux.interval(Duration.ofSeconds(1)).   // 2
						map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date()) + result.toString()), String.class);
	}
}
