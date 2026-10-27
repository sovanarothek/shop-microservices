package com.portfolio.order.controller;

import com.portfolio.order.dto.*;
import com.portfolio.order.model.Order;
import com.portfolio.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "Orders")
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService service;

  @Operation(summary = "Create an order and publish order.created")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Order create(@Valid @RequestBody CreateOrderRequest req) { return service.create(req); }

  @Operation(summary = "List all orders")
  @GetMapping
  public List<Order> list() { return service.findAll(); }

  @Operation(summary = "Get one order")
  @GetMapping("/{id}")
  public Order get(@PathVariable UUID id) { return service.findById(id); }

  @Operation(summary = "Update order status")
  @PatchMapping("/{id}/status")
  public Order status(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest req) {
    return service.updateStatus(id, req.status());
  }
}
