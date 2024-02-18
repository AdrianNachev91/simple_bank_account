package com.bank.bank_account.services;

import com.bank.bank_account.entities.PaymentCard;
import com.bank.bank_account.repositories.PaymentCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;

    public Optional<PaymentCard> getValidPaymentCardForAccount(long accountId, long cardId) {
        return paymentCardRepository.getPaymentCardForAccountIfValid(accountId, cardId);
    }
}
