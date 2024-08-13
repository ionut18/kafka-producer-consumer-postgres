package ro.poc.kafkaconsumerpostgres.mapper;

import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.MetaModel;

public class DocumentEventMapper {

    public static DocumentEvent toEntity(final String key, final MetaModel meta, final DocumentModel model) {
        return DocumentEvent.builder()
                .eventType(meta.getEventType())
                .correlationId(meta.getCorrelationId())
                .timestamp(meta.getTimestamp())
                .creatorApp(meta.getCreatorApp())
                .creatorAppVersion(meta.getCreatorAppVersion())
                .environment(meta.getEnvironment())
                .documentId(key)
                .title(model.getTitle())
                .content(model.getContent())
                .pages(model.getPages())
                .description(model.getDescription())
                .author(model.getAuthor())
                .sentDate(model.getSentDate())
                .build();
    }
}
