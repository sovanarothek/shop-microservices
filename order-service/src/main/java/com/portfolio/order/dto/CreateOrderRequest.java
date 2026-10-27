package com.portfolio.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
    @NotBlank String customerName,
    @NotBlank @Email String customerEmail,
    @NotEmpty List<@Valid Item> items) {

  public record Item(
      @NotBlank String productName,
      @Min(1) int quantity,
      @NotNull @DecimalMin("0.01") BigDecimal unitPrice) {}
}
