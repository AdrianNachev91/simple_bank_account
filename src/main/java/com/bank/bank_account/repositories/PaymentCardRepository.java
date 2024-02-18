package com.bank.bank_account.repositories;

import com.bank.bank_account.entities.PaymentCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends CrudRepository<PaymentCard, Long> {

}
