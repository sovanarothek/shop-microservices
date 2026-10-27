package com.portfolio.order.service;

import com.portfolio.order.dto.CreateOrderRequest;
import com.portfolio.order.event.OrderEventProducer;
import com.portfolio.order.model.*;
import com.portfolio.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
  private static final Set<String> STATUSES = Set.of("NEW", "PAID", "SHIPPED", "CANCELLED");
  private final OrderRepository repo;
  private final OrderEventProducer producer;

  @Transactional
  public Order create(CreateOrderRequest req) {
    Order o = new Order();
    o.setCustomerName(req.customerName());
    o.setCustomerEmail(req.customerEmail());
    BigDecimal total = BigDecimal.ZERO;
    for (var i : req.items()) {
      OrderItem item = new OrderItem();
      item.setProductName(i.productName());
      item.setQuantity(i.quantity());
      item.setUnitPrice(i.unitPrice());
      item.setOrder(o);
      o.getItems().add(item);
      total = total.add(i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())));
    }
    o.setTotalAmount(total);
    Order saved = repo.save(o);
    producer.publishCreated(saved);
    return saved;
  }

  public List<Order> findAll() { return repo.findAll(); }

  public Order findById(UUID id) {
    return repo.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
  }

  @Transactional
  public Order updateStatus(UUID id, String status) {
    if (!STATUSES.contains(status)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
    }
    Order o = findById(id);
    o.setStatus(status);
    o.setUpdatedAt(LocalDateTime.now());
    return o;
  }
}
