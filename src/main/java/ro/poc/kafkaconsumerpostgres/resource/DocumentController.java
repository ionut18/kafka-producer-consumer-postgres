package ro.poc.kafkaconsumerpostgres.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.poc.kafkaconsumerpostgres.messaging.DocumentProducer;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController {

    private final DocumentProducer documentProducer;

    @PostMapping("/create")
    public String createDocument(@RequestBody DocumentModel documentModel) {
        boolean success = documentProducer.send(documentModel);
        return success ? "Success" : "Failed";
    }
}
