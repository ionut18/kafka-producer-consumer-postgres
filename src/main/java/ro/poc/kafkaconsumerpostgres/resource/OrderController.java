package ro.poc.kafkaconsumerpostgres.resource;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.messaging.EventsProducer;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.OrderModel;
import ro.poc.kafkaconsumerpostgres.service.EventGeneratorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final EventsProducer eventsProducer;
    private final EventGeneratorService eventGeneratorService;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @PostMapping("/generate")
    public String generateDocuments(@RequestParam final Integer size) {
        final List<KafkaEvent<OrderModel>> kafkaEvents = eventGeneratorService.generateOrders(size);
        log.info("Generated {} orders", kafkaEvents.size());
        final Boolean success = eventsProducer.send(kafkaEvents, kafkaTopicsConfig.getOrdersTopic());
        return success ? "Success" : "Failed";
    }
}
