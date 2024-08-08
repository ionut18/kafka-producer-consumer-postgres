package ro.poc.kafkaconsumerpostgres.mapper;

import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;

public class DocumentMapper {

    public static Document toEntity(final String key, final DocumentModel model) {
        return Document.builder()
                .id(key)
                .title(model.getTitle())
                .content(model.getContent())
                .pages(model.getPages())
                .description(model.getDescription())
                .author(model.getAuthor())
                .sentDate(model.getSentDate())
                .documentEventType(model.getDocumentEventType())
                .build();
    }
}
