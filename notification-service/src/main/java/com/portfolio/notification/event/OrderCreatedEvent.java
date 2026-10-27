package com.portfolio.notification.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId, String customerName, String customerEmail, BigDecimal totalAmount) {}
