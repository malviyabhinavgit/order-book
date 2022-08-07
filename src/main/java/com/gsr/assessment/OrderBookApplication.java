package com.gsr.assessment;

import com.gsr.assessment.websocket.WebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class OrderBookApplication implements CommandLineRunner {

    @Autowired
    WebSocketClient webSocketClient;

    public static void main(String[] args) {

        SpringApplication.run(OrderBookApplication.class, args);
    }


    @Override
    public void run(String... args) {
        CountDownLatch doneSignal = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            /**
             * Callback for Control-c
             */
            @Override
            public void run() {
                 doneSignal.countDown();
            }
        });
        if (args.length == 0) {
            log.error("Please, provide an instrument name to query. Exiting...");
            System.exit(1);
        }
        else {
            log.info("Streaming order book for instrument: {}, press Ctrl-C to quit.", args[0]);
        }
        String instrument =  args[0];
        webSocketClient.execute(instrument);

        // Block in wait state till unlocked by pressing Control-c
        try {
            doneSignal.await();
            webSocketClient.close();
        } catch (InterruptedException e) {
            webSocketClient.close();
        }

        log.info("Exiting.");
    }
}
