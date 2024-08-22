package ro.poc.kafkaconsumerpostgres.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class KafkaEventDeserializer<T> implements Deserializer<KafkaEvent<T>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> targetType;

    public KafkaEventDeserializer() {
        objectMapper.registerModules(new JavaTimeModule());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        final String targetType = (String) configs.get("targetType");
        try {
            this.targetType = (Class<T>) Class.forName(targetType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to configure deserializer", e);
        }
    }

    @Override
    public KafkaEvent<T> deserialize(final String topic, final byte[] data) {
        try {
            return objectMapper.readValue(data, new TypeReference<KafkaEvent<T>>() {
                @Override
                public Type getType() {
                    return new ParameterizedTypeImpl(KafkaEvent.class, new Type[]{targetType});
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }

    private record ParameterizedTypeImpl(Class<?> rawType, Type[] typeArguments) implements ParameterizedType {

        @Override
            public Type[] getActualTypeArguments() {
                return typeArguments;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }
}
