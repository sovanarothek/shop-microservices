package com.portfolio.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.portfolio.order.dto.CreateOrderRequest;
import com.portfolio.order.event.OrderEventProducer;
import com.portfolio.order.model.Order;
import com.portfolio.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock OrderRepository repo;
  @Mock OrderEventProducer producer;
  @InjectMocks OrderService service;

  @Test
  void createCalculatesTotalAndPublishesEvent() {
    when(repo.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
    var req = new CreateOrderRequest("Sok", "sok@test.com", List.of(
        new CreateOrderRequest.Item("Mouse", 2, new BigDecimal("12.50")),
        new CreateOrderRequest.Item("Keyboard", 1, new BigDecimal("30.00"))));

    Order o = service.create(req);

    assertEquals(0, new BigDecimal("55.00").compareTo(o.getTotalAmount()));
    assertEquals("NEW", o.getStatus());
    assertEquals(2, o.getItems().size());
    verify(producer).publishCreated(o);
  }

  @Test
  void updateStatusChangesStatus() {
    Order order = new Order();
    when(repo.findById(order.getId())).thenReturn(Optional.of(order));

    Order updated = service.updateStatus(order.getId(), "PAID");

    assertEquals("PAID", updated.getStatus());
    assertNotNull(updated.getUpdatedAt());
  }

  @Test
  void updateStatusRejectsInvalidStatus() {
    assertThrows(ResponseStatusException.class,
        () -> service.updateStatus(UUID.randomUUID(), "BOGUS"));
  }

  @Test
  void findByIdThrowsWhenMissing() {
    UUID id = UUID.randomUUID();
    when(repo.findById(id)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class, () -> service.findById(id));
  }
}
x

