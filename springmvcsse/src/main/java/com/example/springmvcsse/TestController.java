package com.example.springmvcsse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TestController {
	@RequestMapping("/test2")
	public SseEmitter handleRequest() {
		final SseEmitter emitter = new SseEmitter();
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(() -> {
			for (int i = 0; i < 1000; i++) {
				try {
					//an HttpMessageConverter will convert BigDecimal in proper format
					emitter.send(new BigDecimal(i));
					emitter.send(" - ", MediaType.TEXT_PLAIN);

					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					emitter.completeWithError(e);
					return;
				}
			}
			emitter.complete();
		});

		return emitter;
	}
}
