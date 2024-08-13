package ro.poc.kafkaconsumerpostgres.service;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.MetaModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DocumentGeneratorService {

    private final Faker faker = new Faker();

    public List<KafkaEvent<DocumentModel>> generateDocuments(final Integer size) {
        final List<KafkaEvent<DocumentModel>> events = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String eventType = i % 3 == 0 ? "DOCUMENT_CREATED" :
                    i % 3 == 1 ? "DOCUMENT_UPDATED" : "DOCUMENT_DELETED";
            String correlationId = UUID.randomUUID().toString();
            LocalDateTime timestamp = LocalDateTime.ofInstant(faker.date().future(30, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
            LocalDateTime sentDate = LocalDateTime.ofInstant(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
            String creatorApp = faker.app().name();
            String creatorAppVersion = faker.app().version();
            String title = faker.book().title();
            String description = faker.lorem().sentence();
            String author = faker.book().author();
            String content = faker.lorem().paragraph();
            Integer pages = faker.number().numberBetween(1, 100);

            // Create JSON structure
            KafkaEvent<DocumentModel> kafkaEvent = new KafkaEvent<>(
                    new MetaModel(eventType, correlationId, timestamp, creatorApp, creatorAppVersion, "local"),
                    new DocumentModel(title, description, author, content, pages, sentDate)
            );
            log.info(kafkaEvent.toString());
            events.add(kafkaEvent);
        }
        return events;
    }
}
