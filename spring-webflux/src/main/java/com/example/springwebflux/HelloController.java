package com.example.springwebflux;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Flux<String> hello() {   // 【改】返回类型为Mono<String>

//        return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(  // 1
//                return Flux.interval(Duration.ofSeconds(1)).   // 2
//                        map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date()));
//                String.class;

//        return Flux.just("Welcome to reactive world ~");     // 【改】使用Mono.just生成响应式数据

        String v = "h,e,l,o,w,l,d";
        List reseult = Lists.newArrayList(v.split(","));

        Thread thread = new Thread(){
            public void run(){
                for (int i=0;i<100;i++){
                    reseult.add(String.valueOf(i));
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();


        return Flux.create(cityFluxSink -> {
            reseult.forEach(s -> {
                cityFluxSink.next((String) s);
            });
            cityFluxSink.complete();
        });
    }
}
