package ro.poc.kafkaconsumerpostgres.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentMapper;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    //todo: batching
    public void save(final String key, final DocumentModel documentModel) {
        final Document document = DocumentMapper.toEntity(key, documentModel);
        documentRepository.save(document);
    }
}
