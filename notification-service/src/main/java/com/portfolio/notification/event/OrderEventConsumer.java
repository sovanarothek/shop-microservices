package com.portfolio.notification.event;

import com.portfolio.notification.model.Notification;
import com.portfolio.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
  private final NotificationRepository repo;

  @KafkaListener(topics = "order.created")
  public void onOrderCreated(OrderCreatedEvent e) {
    Notification n = new Notification();
    n.setOrderId(e.orderId());
    n.setType("ORDER_CREATED");
    n.setRecipientEmail(e.customerEmail());
    n.setMessage("Hi " + e.customerName() + ", your order total is $" + e.totalAmount());
    repo.save(n);
    log.info("Notification saved for order {}", e.orderId());
  }
}
