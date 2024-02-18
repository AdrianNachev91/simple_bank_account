package com.bank.bank_account.services;

import com.bank.bank_account.entities.Account;
import com.bank.bank_account.entities.PaymentCard;
import com.bank.bank_account.entities.TransactionsHistory;
import com.bank.bank_account.entities.enums.CardType;
import com.bank.bank_account.entities.enums.TransactionType;
import com.bank.bank_account.exceptions.BankAccountHttpException;
import com.bank.bank_account.repositories.TransactionHistoryRepository;
import com.bank.bank_account.web.request.TransactionRequest;
import com.bank.bank_account.web.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final PaymentCardService paymentCardService;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionResponse withdraw(long accountId, TransactionRequest transactionRequest) {

        TransactionsHistory transactionsHistory = payOrWithdraw(accountId, transactionRequest);
        transactionsHistory.setTransactionType(TransactionType.WITHDRAWAL);
        transactionHistoryRepository.save(transactionsHistory);

        return TransactionResponse.builder()
                .iban(transactionsHistory.getAccount().getIban())
                .charge(transactionsHistory.getCharge())
                .additionalCharge(transactionsHistory.getAdditionalCharge())
                .build();
    }

    private TransactionsHistory payOrWithdraw(long accountId, TransactionRequest transactionRequest) {

        Optional<Account> optionalAccount = accountService.findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            throw BankAccountHttpException.notFound("Account not found");
        }
        Optional<PaymentCard> optionalPaymentCard = paymentCardService.getValidPaymentCardForAccount(accountId, transactionRequest.paymentCartId());
        if (optionalPaymentCard.isEmpty()) {
            throw BankAccountHttpException.badRequest("Invalid or expired card presented");
        }

        double additionalCharge = 0;
        if (optionalPaymentCard.get().getCardType() == CardType.CREDIT) {
            additionalCharge = transactionRequest.payOrWithdrawAmount().doubleValue() / 100;
        }

        Account account = optionalAccount.get();
        double balanceAfterTransaction = account.getCurrentBalance() - transactionRequest.payOrWithdrawAmount().doubleValue() - additionalCharge;
        if (balanceAfterTransaction < 0) {
            throw BankAccountHttpException.badRequest("Not enough balance");
        }
        account.setCurrentBalance(balanceAfterTransaction);
        accountService.saveAccount(account);

        return TransactionsHistory.builder()
                .account(account)
                .charge(transactionRequest.payOrWithdrawAmount().doubleValue())
                .additionalCharge(additionalCharge)
                .cardType(optionalPaymentCard.get().getCardType())
                .build();
    }
}
