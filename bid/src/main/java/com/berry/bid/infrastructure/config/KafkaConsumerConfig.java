package com.berry.bid.infrastructure.config;


import com.berry.bid.application.model.event.BidEvent;
import com.berry.bid.application.model.event.DeliveryEvent;
import com.berry.bid.application.model.event.PostEvent;
import com.berry.common.exceptionhandler.CustomApiException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.header.Headers;
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

import static com.berry.common.response.ResErrorCode.BAD_REQUEST;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    String kafkaPort;

    @Value("${spring.kafka.consumer.group-id}")
    String groupId;

    public static <T> Map<String, Object> kafkaListenerConfig(String kafkaPort,String groupId,Class<T> listenedClass){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPort);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, listenedClass);
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return configProps;
    }

    @Bean
    public ConsumerFactory<String, Object> postCloseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaListenerConfig(kafkaPort, groupId, PostEvent.Close.class));
    }

    @Bean
    public ConsumerFactory<String, Object> postUpdateConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaListenerConfig(kafkaPort, groupId, PostEvent.Update.class));
    }

    @Bean
    public ConsumerFactory<String, Object> deliveryConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaListenerConfig(kafkaPort, groupId, BidEvent.Delivery.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> postCloseListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postCloseConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> postUpdateListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> deliveryListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(deliveryConsumerFactory());
        return factory;
    }

}
