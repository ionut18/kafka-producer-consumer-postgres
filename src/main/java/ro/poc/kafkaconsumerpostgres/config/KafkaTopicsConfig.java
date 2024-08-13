package ro.poc.kafkaconsumerpostgres.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaTopicsConfig {

    @Value("${kafka.topics.documents.name}")
    private String documentsTopicName;

}
