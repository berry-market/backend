package com.berry.delivery.application.service.notification;

import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.berry.delivery.domain.model.notification.Notification;
import com.berry.delivery.domain.repository.notification.NotificationRepository;
import com.berry.delivery.presentation.dto.request.notification.NotifiactionCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.berry.delivery.presentation.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationDto createNotification(NotifiactionCreateRequest req) {

        Notification notification = Notification.builder()
                .notificationId(req.notificationId())
                .winnerId(req.winnerId())
                .sellerId(req.sellerId())
                .message(req.message())
                .build();

        return NotificationDto.from(notificationRepository.save(notification));
    }

    @Transactional
    public Page<NotificationDto> getAllNotification(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        return notifications.map(NotificationDto::from);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomApiException(ResErrorCode.BAD_REQUEST, "존재하지 않는 알림입니다"));
        notification.markAsDeleted();
    }
}