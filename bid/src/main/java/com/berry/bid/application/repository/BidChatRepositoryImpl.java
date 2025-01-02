package com.berry.bid.application.repository;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.domain.repository.BidChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BidChatRepositoryImpl implements BidChatRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveToSortedSet(String key, BidChat bidChat) {
        redisTemplate.opsForZSet().add(key, bidChat, bidChat.getAmount());
    }

    @Override
    public Optional<BidChat> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BidChat> findAll() {
        return List.of();
    }

    @Override
    public Optional<BidChat> getHighestPrice(String key) {
        return Optional.empty();
    }
}
