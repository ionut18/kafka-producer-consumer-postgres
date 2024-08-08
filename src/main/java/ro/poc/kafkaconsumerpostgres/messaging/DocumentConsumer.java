package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.service.DocumentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer {

    private final DocumentService documentService;

    @KafkaListener(topics = "documents")
    public void consume(final ConsumerRecord<String, DocumentModel> record) {
        //
        log.info("Received key: {}", record.key());
        log.info("Received document: {}", record.value().toString());
        log.info("Received headers: {}", record.headers().toString());
        log.info("Received time: {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()), ZoneId.systemDefault()));
        try {
            documentService.save(record.key(), record.value());
        } catch (Exception e) {
            log.error("Error consuming message {}", e.getMessage(), e);
        }
    }
}
