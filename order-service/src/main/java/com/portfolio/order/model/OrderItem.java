package com.portfolio.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {
  @Id private UUID id = UUID.randomUUID();
  private String productName;
  private int quantity;
  private BigDecimal unitPrice;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id")
  private Order order;
}
