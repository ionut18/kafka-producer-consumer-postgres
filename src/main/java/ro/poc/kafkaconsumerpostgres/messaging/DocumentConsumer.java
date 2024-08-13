package ro.poc.kafkaconsumerpostgres.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.service.DocumentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer {

    private final DocumentService documentService;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopicName()}")
    public void consume(final ConsumerRecord<String, KafkaEvent<DocumentModel>> record) {
        log.info(record.value().toString());
        try {
            documentService.save(record.key(), record.value());
        } catch (Exception e) {
            log.error("Error consuming message {}", e.getMessage(), e);
        }
    }
}
