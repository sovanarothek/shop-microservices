package com.portfolio.notification.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "notifications")
@Getter @Setter @NoArgsConstructor
public class Notification {
  @Id private UUID id = UUID.randomUUID();
  private UUID orderId;
  private String type;
  private String recipientEmail;
  @Column(length = 1000) private String message;
  private LocalDateTime createdAt = LocalDateTime.now();
}
