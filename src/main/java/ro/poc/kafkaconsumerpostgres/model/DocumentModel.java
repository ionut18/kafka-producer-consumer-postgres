package ro.poc.kafkaconsumerpostgres.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class DocumentModel {

    private String title;
    private String description;
    private String author;
    private String content;
    private Long pages;
    private LocalDateTime createdDate;
}
