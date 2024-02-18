package com.bank.bank_account.repositories;


import com.bank.bank_account.entities.TransactionsHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends CrudRepository<TransactionsHistory, Long> {
}
