package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Slf4j
public class DocumentConsumer {

    @KafkaListener(topics = "documents")
    public void consume(final ConsumerRecord<String, DocumentModel> record) {
        log.info("Received key: {}", record.key());
        log.info("Received document: {}", record.value().toString());
        log.info("Received headers: {}", record.headers().toString());
        log.info("Received time: {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()), ZoneId.systemDefault()));
    }
}
