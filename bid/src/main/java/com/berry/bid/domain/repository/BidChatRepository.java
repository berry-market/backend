package com.berry.bid.domain.repository;

import com.berry.bid.application.model.cache.BidChat;

import java.util.List;
import java.util.Optional;

public interface BidChatRepository {

    void saveToSortedSet(String key, BidChat bidChat);

    Optional<BidChat> findById(Long id);

    List<BidChat> findAll();

    Optional<BidChat> getHighestPrice(String key);

}
