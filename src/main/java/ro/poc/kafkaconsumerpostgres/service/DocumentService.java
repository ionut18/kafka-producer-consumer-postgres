package ro.poc.kafkaconsumerpostgres.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentEventMapper;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentMapper;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.repository.DocumentEventRepository;
import ro.poc.kafkaconsumerpostgres.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentEventRepository documentEventRepository;

    //todo: batching
    public void save(final String key, final KafkaEvent<DocumentModel> event) {
        final DocumentEvent documentEvent = DocumentEventMapper.toEntity(key, event.getMeta(), event.getPayload());
        documentEventRepository.save(documentEvent);
        final Document document = DocumentMapper.toEntity(key, event.getPayload());
        documentRepository.save(document);
    }
}
