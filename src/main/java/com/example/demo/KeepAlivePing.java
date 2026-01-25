package com.example.demo; // tua package

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class KeepAlivePing {
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void startPing() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
            	restTemplate.getForObject("https://affitto.torrepalivacanze.it/actuator/health", String.class);
                System.out.println("Keep-alive ping OK");
            } catch (Exception e) {
                System.err.println("Ping failed: " + e.getMessage());
            }
        }, 0, 14, TimeUnit.MINUTES);
    }
}