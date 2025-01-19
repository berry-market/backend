package com.berry.bid.infrastructure.service.message;

import com.berry.bid.application.model.event.BidEvent;
import com.berry.bid.application.service.message.BidConsumerService;
import com.berry.bid.domain.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidConsumerServiceImpl implements BidConsumerService {

    private final BidService bidService;

    @Override
    @KafkaListener(topics = "delivery.status.change", containerFactory = "deliveryListenerContainerFactory")
    public void putAddress(BidEvent.Delivery event) {
        bidService.putAddress(event);
    }

}
