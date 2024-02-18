package com.bank.bank_account.web;

import com.bank.bank_account.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@ActiveProfiles("test")
@Sql({"/cleanup-test-data.sql", "/test-data.sql"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
class AccountControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @DisplayName("Request accounts balance overview")
    @SneakyThrows
    @Test
    void getAccountsBalanceOverview() {

        mockMvc.perform(get("/account/all/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/balance-overview/accounts-balance-overview-200.json")));
    }

    @DisplayName("Withdraw with debit card")
    @SneakyThrows
    @Test
    void withdrawDebit() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-with-debit-200.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-with-debit-200.json")));
    }

    @DisplayName("Withdraw with credit card")
    @SneakyThrows
    @Test
    void withdrawCredit() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-with-credit-200.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-with-credit-200.json")));
    }

    @DisplayName("Withdraw with invalid json")
    @SneakyThrows
    @Test
    void withdrawInvalidJson() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-invalid-json-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-invalid-json-400.json")));
    }

    @DisplayName("Withdraw with validation errors")
    @SneakyThrows
    @Test
    void withdrawValidationErrors() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-validation-errors-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-validation-errors-400.json")));
    }

    @DisplayName("Withdraw with non-existent account")
    @SneakyThrows
    @Test
    void withdrawAccountNotFound() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-with-debit-200.json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-account-not-found-404.json")));
    }

    @DisplayName("Withdraw with invalid card")
    @SneakyThrows
    @Test
    void withdrawInvalidCard() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-with-invalid-card-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-with-invalid-card-400.json")));
    }

    @DisplayName("Withdraw more than current balance")
    @SneakyThrows
    @Test
    void withdrawNotEnoughBalance() {

        mockMvc.perform(post("/account/{accountId}/withdraw", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/withdraw/withdraw-with-debit-overdraft-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/withdraw/withdraw-with-debit-overdraft-400.json")));
    }

    @DisplayName("Transfer with debit card")
    @SneakyThrows
    @Test
    void transferDebit() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-with-debit-200.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-with-debit-200.json")));
    }

    @DisplayName("Transfer with credit card")
    @SneakyThrows
    @Test
    void transferCredit() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-with-credit-200.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-with-credit-200.json")));
    }

    @DisplayName("Transfer with invalid json")
    @SneakyThrows
    @Test
    void transferInvalidJson() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-invalid-json-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-invalid-json-400.json")));
    }

    @DisplayName("Transfer with validation errors")
    @SneakyThrows
    @Test
    void transferValidationErrors() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-validation-errors-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-validation-errors-400.json")));
    }

    @DisplayName("Transfer with non-existent account")
    @SneakyThrows
    @Test
    void transferAccountFromNotFound() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-from-account-not-found-404.json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-account-not-found-404.json")));
    }

    @DisplayName("Transfer to non-existent account")
    @SneakyThrows
    @Test
    void transferToAccountNotFound() {

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-to-account-not-found-404.json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-account-not-found-404.json")));
    }

    @DisplayName("Transfer more than current balance")
    @SneakyThrows
    @Test
    void transferNotEnoughBalance() {

        mockMvc.perform(post("/account/transfer", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.extractJson("request/transfer/transfer-with-debit-overdraft-400.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.extractJson("response/transfer/transfer-with-debit-overdraft-400.json")));
    }
}