package com.ravi.support;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ravi.support.SupportViews.*;

@Service
@Transactional(readOnly = true)
public class SupportService {

    private final SupportRepository repository;

    public SupportService(SupportRepository repository) {
        this.repository = repository;
    }

    public CustomerView getCustomer(long customerId) {
        return repository.findCustomer(customerId)
                .orElseThrow(() ->
                        new NotFoundException("Customer not found."));
    }

    public PageResult<OrderView> listOrders(
            long customerId, int page, int size) {

        getCustomer(customerId);

        return toPage(
                repository.findOrders(customerId, size + 1, page * size),
                page,
                size
        );
    }

    public OrderView getOrder(long customerId, long orderId) {
        getCustomer(customerId);

        return repository.findOrder(customerId, orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order not found for this customer."));
    }

    public ShipmentView getOrderShipment(long customerId, long orderId) {
        getOrder(customerId, orderId);

        return repository.findShipmentByOrder(customerId, orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Shipment not found for this customer."));
    }

    public ShipmentView getShipment(long customerId, String trackingId) {
        getCustomer(customerId);

        return repository.findShipmentByTracking(customerId, trackingId)
                .orElseThrow(() -> new NotFoundException(
                        "Shipment not found for this customer."));
    }

    public PageResult<SubscriptionView> listSubscriptions(
            long customerId, int page, int size) {

        getCustomer(customerId);

        return toPage(
                repository.findSubscriptions(
                        customerId, size + 1, page * size),
                page,
                size
        );
    }

    public SubscriptionView getSubscription(
            long customerId, long subscriptionId) {

        getCustomer(customerId);

        return repository.findSubscription(customerId, subscriptionId)
                .orElseThrow(() -> new NotFoundException(
                        "Subscription not found for this customer."));
    }

    private <T> PageResult<T> toPage(
            List<T> rows, int page, int size) {

        boolean hasNext = rows.size() > size;

        return new PageResult<>(
                List.copyOf(rows.subList(
                        0, Math.min(rows.size(), size))),
                page,
                size,
                hasNext
        );
    }
}