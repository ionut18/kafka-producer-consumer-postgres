package ro.poc.kafkaconsumerpostgres.resource;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ro.poc.kafkaconsumerpostgres.messaging.DocumentProducer;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.service.DocumentGeneratorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentProducer documentProducer;
    private final DocumentGeneratorService documentGeneratorService;

    @PostMapping("/create")
    public String createDocument(@RequestBody final KafkaEvent<DocumentModel> documentModel) {
        boolean success = documentProducer.send(documentModel);
        return success ? "Success" : "Failed";
    }

    @PostMapping("/generate")
    public String generateDocuments(@RequestParam final Integer size) {
        final List<KafkaEvent<DocumentModel>> kafkaEvents = documentGeneratorService.generateDocuments(size);
        log.info("Generated {} documents", kafkaEvents.size());
        final Boolean success = documentProducer.sendEvents(kafkaEvents);
        return success ? "Success" : "Failed";
    }
}
