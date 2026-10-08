package com.ravi.support;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.*;

import static com.ravi.support.SupportViews.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}")
public class SupportController {

    private final SupportService service;

    public SupportController(SupportService service) {
        this.service = service;
    }

    @GetMapping
    public CustomerView customer(
            @PathVariable @Positive long customerId) {

        return service.getCustomer(customerId);
    }

    @GetMapping("/orders")
    public PageResult<OrderView> orders(
            @PathVariable @Positive long customerId,
            @RequestParam(defaultValue = "0")
            @Min(0) @Max(1000000) int page,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int size) {

        return service.listOrders(customerId, page, size);
    }

    @GetMapping("/orders/{orderId}")
    public OrderView order(
            @PathVariable @Positive long customerId,
            @PathVariable @Positive long orderId) {

        return service.getOrder(customerId, orderId);
    }

    @GetMapping("/orders/{orderId}/shipment")
    public ShipmentView orderShipment(
            @PathVariable @Positive long customerId,
            @PathVariable @Positive long orderId) {

        return service.getOrderShipment(customerId, orderId);
    }

    @GetMapping("/shipments/{trackingId}")
    public ShipmentView shipment(
            @PathVariable @Positive long customerId,
            @PathVariable
            @Pattern(regexp = "[A-Z0-9-]{3,40}") String trackingId) {

        return service.getShipment(customerId, trackingId);
    }

    @GetMapping("/subscriptions")
    public PageResult<SubscriptionView> subscriptions(
            @PathVariable @Positive long customerId,
            @RequestParam(defaultValue = "0")
            @Min(0) @Max(1000000) int page,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int size) {

        return service.listSubscriptions(customerId, page, size);
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    public SubscriptionView subscription(
            @PathVariable @Positive long customerId,
            @PathVariable @Positive long subscriptionId) {

        return service.getSubscription(customerId, subscriptionId);
    }
}