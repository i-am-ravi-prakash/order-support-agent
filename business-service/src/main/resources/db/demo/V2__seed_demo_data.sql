-- Synthetic snapshot for local demos. These dates are intentionally fixed.

INSERT INTO business.customers (id, name, email) VALUES
                                                     (1, 'Ravi Demo', 'ravi@example.com'),
                                                     (2, 'Asha Demo', 'asha@example.com');

INSERT INTO business.orders
(id, order_number, customer_id, product_type, product_name,
 quantity, total_amount, currency, status, created_at)
VALUES
    (101, 'ORD-101', 1, 'PRINTER', 'Demo Printer Rental',
     1, 799.00, 'INR', 'SHIPPED', '2026-10-01 09:00:00+00'),

    (102, 'ORD-102', 1, 'INK', 'Demo Ink Monthly Plan',
     1, 299.00, 'INR', 'DELIVERED', '2026-10-02 09:00:00+00'),

    (103, 'ORD-103', 1, 'PAPER', 'Demo Paper Monthly Plan',
     1, 199.00, 'INR', 'PROCESSING', '2026-10-03 09:00:00+00'),

    (201, 'ORD-201', 2, 'PRINTER', 'Demo Printer Annual Rental',
     1, 8999.00, 'INR', 'SHIPPED', '2026-10-04 09:00:00+00');

INSERT INTO business.shipments
(id, order_id, tracking_id, carrier, status,
 estimated_delivery_date, delivered_date, delay_reason, updated_at)
VALUES
    (1001, 101, 'TRK-101', 'DemoShip', 'DELAYED',
     '2026-10-07', NULL, 'Shipment held at the regional sorting hub.',
     '2026-10-08 08:00:00+00'),

    (1002, 102, 'TRK-102', 'DemoShip', 'DELIVERED',
     '2026-10-06', '2026-10-05', NULL, '2026-10-05 10:00:00+00'),

    (2001, 201, 'TRK-201', 'DemoShip', 'IN_TRANSIT',
     '2026-10-10', NULL, NULL, '2026-10-08 07:00:00+00');

INSERT INTO business.subscriptions
(id, customer_id, source_order_id, product_type, plan_name,
 billing_cycle, status, recurring_amount, currency, start_date,
 next_billing_date, minimum_term_end_date, cancelled_date)
VALUES
    (501, 1, 101, 'PRINTER', 'Printer Monthly Rental',
     'MONTHLY', 'ACTIVE', 799.00, 'INR', '2026-10-01',
     '2026-11-01', '2027-09-30', NULL),

    (502, 1, 102, 'INK', 'Ink Monthly Supply',
     'MONTHLY', 'ACTIVE', 299.00, 'INR', '2026-10-02',
     '2026-11-02', NULL, NULL),

    (503, 1, 103, 'PAPER', 'Paper Monthly Supply',
     'MONTHLY', 'PAUSED', 199.00, 'INR', '2026-10-03',
     NULL, NULL, NULL),

    (601, 2, 201, 'PRINTER', 'Printer Annual Rental',
     'ANNUAL', 'ACTIVE', 8999.00, 'INR', '2026-10-04',
     '2027-10-04', '2027-10-03', NULL);

-- Keep generated IDs above the explicit seed IDs for later write APIs.

SELECT setval(pg_get_serial_sequence('business.customers', 'id'), 2);
SELECT setval(pg_get_serial_sequence('business.orders', 'id'), 201);
SELECT setval(pg_get_serial_sequence('business.shipments', 'id'), 2001);
SELECT setval(pg_get_serial_sequence('business.subscriptions', 'id'), 601);