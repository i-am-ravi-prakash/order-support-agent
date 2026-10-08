package com.ravi.support;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public final class SupportViews {

    private SupportViews() {}

    public record CustomerView(
            long id,
            String name,
            String email) {}

    public record OrderView(
            long id,
            String orderNumber,
            long customerId,
            String productType,
            String productName,
            int quantity,
            BigDecimal totalAmount,
            String currency,
            String status,
            OffsetDateTime createdAt) {}

    public record ShipmentView(
            long id,
            long orderId,
            String trackingId,
            String carrier,
            String status,
            LocalDate estimatedDeliveryDate,
            LocalDate deliveredDate,
            String delayReason,
            OffsetDateTime updatedAt) {}

    public record SubscriptionView(
            long id,
            long customerId,
            long sourceOrderId,
            String productType,
            String planName,
            String billingCycle,
            String status,
            BigDecimal recurringAmount,
            String currency,
            LocalDate startDate,
            LocalDate nextBillingDate,
            LocalDate minimumTermEndDate,
            LocalDate cancelledDate) {}

    public record PageResult<T>(
            List<T> items,
            int page,
            int size,
            boolean hasNext) {}
}