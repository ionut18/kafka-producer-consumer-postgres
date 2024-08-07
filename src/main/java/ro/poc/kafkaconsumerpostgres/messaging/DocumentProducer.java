package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentProducer {

    private final KafkaTemplate<String, DocumentModel> kafkaTemplate;

    public boolean send(final DocumentModel document) {
        log.info("Sending document: {}", document);
        try {
            kafkaTemplate.send("documents", UUID.randomUUID().toString(), document);
            log.info("Document sent");
            return true;
        } catch (Exception e) {
            log.error("Error sending document to kafka {}", e.getMessage(), e);
        }
        return false;
    }
}
