package com.ravi.support;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ravi.support.SupportViews.*;

@Repository
public class SupportRepository {

    private final JdbcClient jdbc;

    public SupportRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<CustomerView> findCustomer(long customerId) {
        return jdbc.sql("""
                SELECT id, name, email FROM business.customers
                WHERE id = :customerId
                """)
                .param("customerId", customerId)
                .query(CustomerView.class)
                .optional();
    }

    public List<OrderView> findOrders(
            long customerId, int limit, int offset) {

        return jdbc.sql("""
                SELECT id, order_number, customer_id, product_type,
                       product_name, quantity, total_amount, currency,
                       status, created_at
                FROM business.orders
                WHERE customer_id = :customerId
                ORDER BY created_at DESC, id DESC
                LIMIT :limit OFFSET :offset
                """)
                .param("customerId", customerId)
                .param("limit", limit)
                .param("offset", offset)
                .query(OrderView.class)
                .list();
    }

    public Optional<OrderView> findOrder(long customerId, long orderId) {
        return jdbc.sql("""
                SELECT id, order_number, customer_id, product_type,
                       product_name, quantity, total_amount, currency,
                       status, created_at
                FROM business.orders
                WHERE id = :orderId AND customer_id = :customerId
                """)
                .param("orderId", orderId)
                .param("customerId", customerId)
                .query(OrderView.class)
                .optional();
    }

    public Optional<ShipmentView> findShipmentByOrder(
            long customerId, long orderId) {

        return jdbc.sql("""
                SELECT s.id, s.order_id, s.tracking_id, s.carrier, s.status,
                       s.estimated_delivery_date, s.delivered_date,
                       s.delay_reason, s.updated_at
                FROM business.shipments s
                JOIN business.orders o ON o.id = s.order_id
                WHERE o.customer_id = :customerId AND o.id = :orderId
                """)
                .param("customerId", customerId)
                .param("orderId", orderId)
                .query(ShipmentView.class)
                .optional();
    }

    public Optional<ShipmentView> findShipmentByTracking(
            long customerId, String trackingId) {

        return jdbc.sql("""
                SELECT s.id, s.order_id, s.tracking_id, s.carrier, s.status,
                       s.estimated_delivery_date, s.delivered_date,
                       s.delay_reason, s.updated_at
                FROM business.shipments s
                JOIN business.orders o ON o.id = s.order_id
                WHERE o.customer_id = :customerId
                  AND s.tracking_id = :trackingId
                """)
                .param("customerId", customerId)
                .param("trackingId", trackingId)
                .query(ShipmentView.class)
                .optional();
    }

    public List<SubscriptionView> findSubscriptions(
            long customerId, int limit, int offset) {

        return jdbc.sql("""
                SELECT id, customer_id, source_order_id, product_type,
                       plan_name, billing_cycle, status, recurring_amount,
                       currency, start_date, next_billing_date,
                       minimum_term_end_date, cancelled_date
                FROM business.subscriptions
                WHERE customer_id = :customerId
                ORDER BY id ASC
                LIMIT :limit OFFSET :offset
                """)
                .param("customerId", customerId)
                .param("limit", limit)
                .param("offset", offset)
                .query(SubscriptionView.class)
                .list();
    }

    public Optional<SubscriptionView> findSubscription(
            long customerId, long subscriptionId) {

        return jdbc.sql("""
                SELECT id, customer_id, source_order_id, product_type,
                       plan_name, billing_cycle, status, recurring_amount,
                       currency, start_date, next_billing_date,
                       minimum_term_end_date, cancelled_date
                FROM business.subscriptions
                WHERE customer_id = :customerId AND id = :subscriptionId
                """)
                .param("customerId", customerId)
                .param("subscriptionId", subscriptionId)
                .query(SubscriptionView.class)
                .optional();
    }
}