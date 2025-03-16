package com.challenge.entrypoint.kafka.deserializers;

public class DeserializationException extends RuntimeException {
    public DeserializationException(String message) {
        super(message);
    }
}
