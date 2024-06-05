package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.NotificationRequest;
import com.thanhtan.groceryshop.dto.response.NotificationResponse;

import java.util.List;

public interface INotificationService {
    void sendNotificationToAdmin(NotificationRequest notificationRequest);

    void sendNotificationToUser(NotificationRequest notificationRequest, List<Long> userIds);

    List<NotificationResponse> getNotificationsByUser();

    NotificationResponse markAsSeen(Long notificationId);


}
