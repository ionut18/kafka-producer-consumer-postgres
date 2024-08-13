package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentProducer {

    private final KafkaTemplate<String, KafkaEvent<DocumentModel>> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    public Boolean sendEvents(final List<KafkaEvent<DocumentModel>> events) {
        return events.stream().map(this::send).anyMatch(FALSE::equals);
    }

    public Boolean send(final KafkaEvent<DocumentModel> event) {
        try {
            kafkaTemplate.send(kafkaTopicsConfig.getDocumentsTopicName(),
                    UUID.randomUUID().toString(),
                    event);
            log.info("Document sent successfully {}", event);
            return TRUE;
        } catch (Exception e) {
            log.error("Error sending document to kafka {}", e.getMessage(), e);
        }
        return FALSE;
    }
}
