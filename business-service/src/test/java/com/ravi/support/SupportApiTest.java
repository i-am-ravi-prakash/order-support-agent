package com.ravi.support;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"local", "test"})
class SupportApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void customerAndOwnedOrderAreReturned() throws Exception {
        mvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravi Demo"));

        mvc.perform(get("/api/v1/customers/1/orders/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(799.00));
    }

    @Test
    void ordersAreScopedAndPaginated() throws Exception {
        mvc.perform(get("/api/v1/customers/1/orders?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id").value(103))
                .andExpect(jsonPath("$.items[1].id").value(102))
                .andExpect(jsonPath("$.hasNext").value(true));

        mvc.perform(get("/api/v1/customers/1/orders?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.hasNext").value(false));

        mvc.perform(get("/api/v1/customers/2/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id").value(201));
    }

    @Test
    void delayedShipmentIncludesRecordedReason() throws Exception {
        mvc.perform(get("/api/v1/customers/1/shipments/TRK-101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELAYED"))
                .andExpect(jsonPath("$.delayReason")
                        .value("Shipment held at the regional sorting hub."));

        mvc.perform(get("/api/v1/customers/1/orders/101/shipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingId").value("TRK-101"));
    }

    @Test
    void deliveredShipmentIncludesDeliveryDate() throws Exception {
        mvc.perform(get("/api/v1/customers/1/shipments/TRK-102"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"))
                .andExpect(jsonPath("$.deliveredDate").value("2026-10-05"));
    }

    @Test
    void subscriptionPlansAndNullableDatesAreReturned() throws Exception {
        mvc.perform(get("/api/v1/customers/1/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].billingCycle").value("MONTHLY"))
                .andExpect(jsonPath("$.items[2].status").value("PAUSED"));

        mvc.perform(get("/api/v1/customers/1/subscriptions/501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minimumTermEndDate").value("2027-09-30"));

        mvc.perform(get("/api/v1/customers/2/subscriptions/601"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billingCycle").value("ANNUAL"));
    }

    @Test
    void unknownResourcesReturn404() throws Exception {
        for (String path : new String[]{
                "/api/v1/customers/999",
                "/api/v1/customers/999/orders",
                "/api/v1/customers/1/orders/999",
                "/api/v1/customers/1/orders/103/shipment",
                "/api/v1/customers/1/shipments/TRK-UNKNOWN",
                "/api/v1/customers/1/subscriptions/999"
        }) {
            mvc.perform(get(path))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Test
    void resourcesFromAnotherCustomerReturn404() throws Exception {
        for (String path : new String[]{
                "/api/v1/customers/1/orders/201",
                "/api/v1/customers/1/orders/201/shipment",
                "/api/v1/customers/1/shipments/TRK-201",
                "/api/v1/customers/1/subscriptions/601"
        }) {
            mvc.perform(get(path))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Test
    void invalidIdsAndPaginationReturn400() throws Exception {
        for (String path : new String[]{
                "/api/v1/customers/0/orders",
                "/api/v1/customers/abc/orders",
                "/api/v1/customers/1/orders/-1",
                "/api/v1/customers/1/orders?page=-1",
                "/api/v1/customers/1/orders?size=0",
                "/api/v1/customers/1/orders?size=101",
                "/api/v1/customers/1/orders?page=1000001",
                "/api/v1/customers/1/subscriptions?size=101"
        }) {
            mvc.perform(get(path))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
    }

    @Test
    void databaseReadinessIsUp() throws Exception {
        mvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}