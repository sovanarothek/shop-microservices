package com.portfolio.order.event;

import com.portfolio.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {
  public static final String TOPIC = "order.created";
  private final KafkaTemplate<String, Object> kafka;

  public void publishCreated(Order o) {
    var event = new OrderCreatedEvent(
        o.getId(), o.getCustomerName(), o.getCustomerEmail(), o.getTotalAmount());
    try {
      kafka.send(TOPIC, o.getId().toString(), event);
    } catch (Exception e) {
      log.warn("Kafka unavailable, event not sent: {}", e.getMessage());
    }
  }
}
