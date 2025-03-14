package com.challenge.entrypoint.kafka.config;

import com.challenge.entrypoint.kafka.deserializers.CreateOrderEventDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    public static final String ORDER_REQUESTED_EVENT_FACTORY = "ORDER_REQUESTED_EVENT_FACTORY";

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Bean("kafkaListenerConsumerFactory")
    public ConsumerFactory<String, String> consumerFactory() {
        final var kafkaProperties = new KafkaProperties();
        final var props = kafkaProperties.buildConsumerProperties((new DefaultSslBundleRegistry()));

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CreateOrderEventDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(ORDER_REQUESTED_EVENT_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, String> createOrderEventFactory(
            DefaultErrorHandler errorHandler) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();

        factory.setCommonErrorHandler(errorHandler);
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
    }
}
