package ro.poc.kafkaconsumerpostgres.resource;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.messaging.EventsProducer;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.service.EventGeneratorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private final EventsProducer eventsProducer;
    private final EventGeneratorService eventGeneratorService;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @PostMapping("/create")
    public String createDocument(@RequestBody final KafkaEvent<DocumentModel> documentModel) {
        boolean success = eventsProducer.send(documentModel, kafkaTopicsConfig.getDocumentsTopic());
        return success ? "Success" : "Failed";
    }

    @PostMapping("/generate")
    public String generateDocuments(@RequestParam final Integer size) {
        final List<KafkaEvent<DocumentModel>> kafkaEvents = eventGeneratorService.generateDocuments(size);
        log.info("Generated {} documents", kafkaEvents.size());
        final Boolean success = eventsProducer.send(kafkaEvents, kafkaTopicsConfig.getDocumentsTopic());
        return success ? "Success" : "Failed";
    }
}
