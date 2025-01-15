package com.berry.delivery.presentation.controller.notification;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.delivery.application.service.notification.NotificationService;
import com.berry.delivery.presentation.dto.NotificationDto;
import com.berry.delivery.presentation.dto.request.notification.NotificationCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ApiResponse<?> createNotification(@Valid @RequestBody NotificationCreateRequest req){
        notificationService.createNotification(req);
        return ApiResponse.OK(ResSuccessCode.CREATED,"알림이 생성되었습니다");
    }

    @GetMapping
    public ApiResponse<?> getAllNotification(Pageable pageable){
        Page<NotificationDto> notifications = notificationService.getAllNotification(pageable);
        return ApiResponse.OK(ResSuccessCode.READ,notifications);
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<?> deleteNotification(@PathVariable Long notificationId){
        notificationService.deleteNotification(notificationId);
        return ApiResponse.OK(ResSuccessCode.DELETED,"알림이 삭제되었습니다.");
    }
}