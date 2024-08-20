package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventsProducer {

    private final KafkaTemplate<String, KafkaEvent<?>> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    public Boolean send(final List<? extends KafkaEvent<?>> events) {
        return events.stream().map(this::send).noneMatch(FALSE::equals);
    }

    public Boolean send(final KafkaEvent<?> event) {
        try {
            kafkaTemplate.send(kafkaTopicsConfig.getDocumentsTopic(),
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
