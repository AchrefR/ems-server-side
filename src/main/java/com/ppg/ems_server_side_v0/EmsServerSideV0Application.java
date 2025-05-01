package com.ppg.ems_server_side_v0;

import com.ppg.ems_server_side_v0.config.migration.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootApplication
public class EmsServerSideV0Application implements CommandLineRunner {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(EmsServerSideV0Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        this.applicationEventPublisher.publishEvent(new Notifier());
    }
}
