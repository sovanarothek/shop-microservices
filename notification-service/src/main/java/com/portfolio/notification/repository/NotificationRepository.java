package com.portfolio.notification.repository;

import com.portfolio.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findByOrderId(UUID orderId);
}
