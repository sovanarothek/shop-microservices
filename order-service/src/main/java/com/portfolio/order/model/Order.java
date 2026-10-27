package com.portfolio.order.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class Order {
  @Id private UUID id = UUID.randomUUID();
  private String customerName;
  private String customerEmail;
  private BigDecimal totalAmount;
  private String status = "NEW";
  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();
}
