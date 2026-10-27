package com.portfolio.notification.controller;

import com.portfolio.notification.model.Notification;
import com.portfolio.notification.repository.NotificationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "Notifications")
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationRepository repo;

  @Operation(summary = "List notifications, optionally by order")
  @GetMapping
  public List<Notification> list(@RequestParam(required = false) UUID orderId) {
    return orderId == null ? repo.findAll() : repo.findByOrderId(orderId);
  }
}
