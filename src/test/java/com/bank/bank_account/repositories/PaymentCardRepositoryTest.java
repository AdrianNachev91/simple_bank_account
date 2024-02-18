package com.bank.bank_account.repositories;

import com.bank.bank_account.entities.PaymentCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext
@ActiveProfiles("test")
@Sql({"/cleanup-test-data.sql", "/test-data.sql"})
class PaymentCardRepositoryTest {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @DisplayName("Valid payment card for account found")
    @Test
    void getPaymentCardForAccountIfValidFound() {

        // Test
        Optional<PaymentCard> paymentCard = paymentCardRepository.getPaymentCardForAccountIfValid(1L, 1L);

        // Verify
        assertThat(paymentCard).isNotEmpty();
        assertThat(paymentCard.get().getId()).isEqualTo(1);
        assertThat(paymentCard.get().getAccount().getId()).isEqualTo(1);
    }

    @DisplayName("No valid payment card for account found")
    @Test
    void getPaymentCardForAccountIfValidNotFound() {

        // Test
        Optional<PaymentCard> paymentCard = paymentCardRepository.getPaymentCardForAccountIfValid(1L, 5L);

        // Verify
        assertThat(paymentCard).isEmpty();
    }
}