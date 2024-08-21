package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
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

    public Boolean send(final List<? extends KafkaEvent<?>> events, final String topic) {
        return events.stream().map(event -> send(event, topic)).noneMatch(FALSE::equals);
    }

    public Boolean send(final KafkaEvent<?> event, final String topic) {
        try {
            kafkaTemplate.send(topic,
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
