package com.bank.bank_account.repositories;

import com.bank.bank_account.entities.PaymentCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentCardRepository extends CrudRepository<PaymentCard, Long> {

    @Query("SELECT p FROM payment_card p JOIN p.account a " +
            "WHERE a.id = :accountId " +
            "AND p.id = :cardId " +
            "AND p.expiryDate > current_time")
    Optional<PaymentCard> getPaymentCardForAccountIfValid(@Param("accountId") Long accountId, @Param("cardId") Long cardId);
}
