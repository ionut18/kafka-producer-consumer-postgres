package ro.poc.kafkaconsumerpostgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import ro.poc.kafkaconsumerpostgres.model.DocumentEventType;


import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    private String id;
    private String title;
    private String description;
    private String author;
    private String content;
    private Integer pages;
    private LocalDateTime sentDate;

    //todo: move it to meta fields with timestamp, creator app, environment, etc
    @Enumerated(EnumType.STRING)
    private DocumentEventType documentEventType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
