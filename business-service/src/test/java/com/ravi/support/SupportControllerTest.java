package com.ravi.support;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.CannotCreateTransactionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportController.class)
class SupportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SupportService service;

    @Test
    void invalidParametersReturn400() throws Exception {
        for (String path : new String[]{
                "/api/v1/customers/abc/orders",
                "/api/v1/customers/1/orders?size=101",
                "/api/v1/customers/1/shipments/invalid"
        }) {
            mvc.perform(get(path))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
    }

    @Test
    void databaseFailuresReturn503WithoutDriverDetails() throws Exception {
        for (RuntimeException failure : new RuntimeException[]{
                new DataAccessResourceFailureException("password=secret-value"),
                new CannotCreateTransactionException("password=secret-value")
        }) {
            when(service.getCustomer(1)).thenThrow(failure);

            String body = mvc.perform(get("/api/v1/customers/1"))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$.code").value("DATABASE_UNAVAILABLE"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(body).doesNotContain("secret-value");

            org.mockito.Mockito.reset(service);
        }
    }
}