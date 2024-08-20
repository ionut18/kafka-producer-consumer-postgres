package ro.poc.kafkaconsumerpostgres.service;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class EventGeneratorService {

    private final Faker faker = new Faker();

    public List<KafkaEvent<DocumentModel>> generateDocuments(final Integer size) {
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    final String eventType = i % 3 == 0 ? "DOCUMENT_CREATED" :
                            i % 3 == 1 ? "DOCUMENT_UPDATED" : "DOCUMENT_DELETED";
                    final MetaModel metaModel = generateMetaModel(eventType);

                    final String title = faker.book().title();
                    final String description = faker.lorem().sentence();
                    final String author = faker.book().author();
                    final String content = faker.lorem().paragraph();
                    final Integer pages = faker.number().numberBetween(1, 100);
                    final LocalDateTime sentDate = LocalDateTime.ofInstant(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
                    final DocumentModel documentModel = new DocumentModel(title, description, author, content, pages, sentDate);

                    final KafkaEvent<DocumentModel> kafkaEvent = new KafkaEvent<>(metaModel, documentModel);
                    log.info(kafkaEvent.toString());
                    return kafkaEvent;
                })
                .collect(Collectors.toList());

    }

    public List<KafkaEvent<OrderModel>> generateOrders(final Integer size) {
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    final String eventType = i % 3 == 0 ? "ORDER_CREATED" :
                            i % 3 == 1 ? "ORDER_UPDATED" : "ORDER_PAID";
                    final MetaModel metaModel = generateMetaModel(eventType);

                    final String id = UUID.randomUUID().toString();
                    final String customerId = UUID.randomUUID().toString();
                    final LocalDateTime date = LocalDateTime.ofInstant(faker.date().past(30, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
                    final BigDecimal totalValue = new BigDecimal(faker.number().numberBetween(1, 10000));
                    final String payment = i % 2 == 0 ? "CARD" : "CASH";
                    final List<ProductModel> products = generateProducts(2);
                    final OrderModel orderModel = new OrderModel(id, customerId, totalValue, date, payment, products);

                    final KafkaEvent<OrderModel> kafkaEvent = new KafkaEvent<>(metaModel, orderModel);
                    log.info(kafkaEvent.toString());
                    return kafkaEvent;
                })
                .collect(Collectors.toList());
    }

    private List<ProductModel> generateProducts(final Integer size) {
        return IntStream.range(0, size)
                .mapToObj(_ -> {
                    final String productId = UUID.randomUUID().toString();
                    final String name = faker.commerce().productName();
                    final BigDecimal price = new BigDecimal(faker.number().numberBetween(1, 10000));
                    final String categoryId = UUID.randomUUID().toString();
                    return new ProductModel(productId, name, price, categoryId);
                })
                .collect(Collectors.toList());

    }

    private MetaModel generateMetaModel(final String eventType) {
        final String correlationId = UUID.randomUUID().toString();
        final LocalDateTime timestamp = LocalDateTime.ofInstant(faker.date().future(30, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
        final String creatorApp = faker.app().name();
        final String creatorAppVersion = faker.app().version();
        return new MetaModel(eventType, correlationId, timestamp, creatorApp, creatorAppVersion, "local");
    }
}
