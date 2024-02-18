package com.bank.bank_account.services;

import com.bank.bank_account.entities.PaymentCard;
import com.bank.bank_account.repositories.PaymentCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceTest {

    PaymentCardService paymentCardService;

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @BeforeEach
    void setUp() {
        paymentCardService = new PaymentCardService(paymentCardRepository);
    }

    @DisplayName("Get valid payment card for account")
    @Test
    void getValidPaymentCardsForAccount() {

        // Prepare
        var paymentCard = Optional.of(PaymentCard.builder().id(1L).build());
        when(paymentCardRepository.getPaymentCardForAccountIfValid(1L, 1L)).thenReturn(paymentCard);

        // Test
        Optional<PaymentCard> result = paymentCardService.getValidPaymentCardForAccount(1L, 1L);

        // Verify
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(1);
    }
}