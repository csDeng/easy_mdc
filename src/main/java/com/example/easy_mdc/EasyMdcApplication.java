package com.example.easy_mdc;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@SpringBootApplication
@RestController
@EnableAspectJAutoProxy
@Slf4j
public class EasyMdcApplication {



    public static void main(String[] args) {
        SpringApplication.run(EasyMdcApplication.class, args);
        System.out.println("============ application start successfully ==============");
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("h1>>>>>");
        return "hello";
    }

    @Resource
    @Qualifier("taskExecutor")
    private Executor executor;

    @PostMapping("/pool")
    public String pool() throws Exception{
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("线程池里面");
            try{
                Thread.sleep(1000);
            } catch (Exception ignored){}

            return "";
        }, executor);
        future.thenApplyAsync( value ->{
            log.info("线程池组合操作");
            try{
                Thread.sleep(1000);
            } catch (Exception ignored) {}
            return value + "1";
        }, executor);
        return future.get();
    }


}
