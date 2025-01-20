package com.berry.user.infrastructure.config;

import com.berry.user.application.model.event.UserEvent;
import com.berry.user.infrastructure.model.PaymentEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    String kafkaPort;

    @Value("${spring.kafka.consumer.group-id}")
    String groupId;

    private Map<String, Object> commonConsumerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPort);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configProps;
    }

    @Bean
    public ConsumerFactory<String, PaymentEvent> paymentCompletedEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(commonConsumerConfig());
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentEvent.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, PaymentEvent> paymentCanceledEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(commonConsumerConfig());
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentEvent.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, UserEvent.Bidding> userEventBiddingConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(commonConsumerConfig());
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserEvent.Bidding.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentCompletedEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentCompletedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentCanceledEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentCompletedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent.Bidding> userEventBiddingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent.Bidding> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userEventBiddingConsumerFactory());
        return factory;
    }

}
