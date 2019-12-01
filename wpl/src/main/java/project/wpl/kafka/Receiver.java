package project.wpl.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class Receiver {

Logger logger = LoggerFactory.getLogger(Receiver.class);

    @KafkaListener(topics = "wpl", groupId = "project")
    public void receive(String payload) {
       System.out.println("in receiver "+payload);
        logger.info("Kafka consumer "+payload);
    }
}
