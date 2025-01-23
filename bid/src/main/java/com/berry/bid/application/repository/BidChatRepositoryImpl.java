package com.berry.bid.application.repository;

import com.berry.bid.application.model.cache.BidChat;
import com.berry.bid.domain.repository.BidChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BidChatRepositoryImpl implements BidChatRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveToSortedSet(String key, BidChat bidChat) {
        redisTemplate.opsForZSet().add(key, bidChat, bidChat.getAmount());
    }

    @Override
    public List<BidChat> findAll(String key) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        Set<Object> elements = zSetOps.range(key, 0, -1);
        return elements.stream()
                .map(e -> (BidChat) e)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BidChat> getHighestPrice(String key) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        // 가장 큰 값 하나만 가져오기
        Set<Object> result = zSetOps.reverseRange(key, 0, 0);

        if (result != null && !result.isEmpty()) {
            Object value = result.iterator().next();
            if (value instanceof BidChat) {
                return Optional.of((BidChat) value);
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveImmediatePrice(String key, Integer price) {
        redisTemplate.opsForValue().set(key, String.valueOf(price));
    }

    @Override
    public Integer findImmediatePrice(String key) {
        return Integer.parseInt((String) redisTemplate.opsForValue().get(key));
    }

}
