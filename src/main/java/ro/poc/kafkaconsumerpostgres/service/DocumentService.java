package ro.poc.kafkaconsumerpostgres.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentEventMapper;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentMapper;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.repository.DocumentEventRepository;
import ro.poc.kafkaconsumerpostgres.repository.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentEventRepository documentEventRepository;

    public void save(final String key, final KafkaEvent<DocumentModel> event) {
        final DocumentEvent documentEvent = DocumentEventMapper.toEntity(key, event);
        documentEventRepository.save(documentEvent);
        final Document document = DocumentMapper.toEntity(key, event.getPayload());
        documentRepository.save(document);
    }

    public void saveAll(final List<ConsumerRecord<String, KafkaEvent<DocumentModel>>> records) {
        final List<DocumentEvent> documentEvents = records.stream()
                .map(record -> DocumentEventMapper.toEntity(record.key(), record.value()))
                .collect(Collectors.toList());
        documentEventRepository.saveAll(documentEvents);
        final List<Document> documents= records.stream()
                .map(record -> DocumentMapper.toEntity(record.key() + "batch", record.value().getPayload()))
                .collect(Collectors.toList());
        documentRepository.saveAll(documents);
    }
}
