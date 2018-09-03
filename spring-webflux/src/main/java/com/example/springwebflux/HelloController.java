package com.example.springwebflux;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class HelloController {
    @GetMapping("/hello2")
    public Mono<String> hello2() {   // 【改】返回类型为Mono<String>
        Mono<String> rs = Mono.just("Welcome to reactive world ~");
        return rs;
    }

    @RequestMapping(value="/push",produces="text/event-stream;charset=utf-8")
    @ResponseBody
    public String push() {
        Random r = new Random();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "data:Testing 1,2,3" + r.nextInt() +"\n\n";
    }

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

        Flux<String> ab = Flux.interval(Duration.ofSeconds(1)).
                map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date()))
//                .create(s->{
//                    reseult.forEach(f -> {
//                        s.next((String) f);
//                    });
//                    s.complete();
//                })
 ;

//        Flux.create(cityFluxSink -> {
//            reseult.forEach(s -> {
//                cityFluxSink.next((String) s);
//            });
//            cityFluxSink.complete();
//        });
        return ab;
    }
}
