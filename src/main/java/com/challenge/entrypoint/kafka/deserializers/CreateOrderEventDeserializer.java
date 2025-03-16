package com.challenge.entrypoint.kafka.deserializers;

import com.challenge.entrypoint.kafka.events.CreateOrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CreateOrderEventDeserializer implements Deserializer<CreateOrderEvent> {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private ObjectMapper objectMapper = null;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public CreateOrderEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, CreateOrderEvent.class);
        } catch (Exception e) {
            logger.error("Failed deserializing: {}", new String(data), e);
            return null;
        }
    }

    @Override
    public void close() {
        objectMapper = null;
    }
}