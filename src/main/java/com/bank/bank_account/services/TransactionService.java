package com.bank.bank_account.services;

import com.bank.bank_account.entities.Account;
import com.bank.bank_account.entities.PaymentCard;
import com.bank.bank_account.entities.TransactionsHistory;
import com.bank.bank_account.entities.enums.CardType;
import com.bank.bank_account.entities.enums.TransactionType;
import com.bank.bank_account.exceptions.BankAccountHttpException;
import com.bank.bank_account.repositories.TransactionHistoryRepository;
import com.bank.bank_account.web.request.TransferRequest;
import com.bank.bank_account.web.request.WithdrawRequest;
import com.bank.bank_account.web.response.TransferResponse;
import com.bank.bank_account.web.response.WithdrawResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountService accountService;
    private final PaymentCardService paymentCardService;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public WithdrawResponse withdraw(long accountId, WithdrawRequest withdrawRequest) {

        Account account = getAccount(accountId);
        PaymentCard paymentCard = getPaymentCard(accountId, withdrawRequest.paymentCartId());
        double additionalCharge = calculateAdditionalCharge(withdrawRequest.payOrWithdrawAmount().doubleValue(), paymentCard);
        double balanceAfterTransaction = calculateNewBalance(withdrawRequest.payOrWithdrawAmount().doubleValue(), account.getCurrentBalance(), additionalCharge);

        account.setCurrentBalance(balanceAfterTransaction);
        accountService.saveAccount(account);

        transactionHistoryRepository.save(TransactionsHistory.builder()
                .fromAccount(account)
                .charge(withdrawRequest.payOrWithdrawAmount().doubleValue())
                .additionalCharge(additionalCharge)
                .cardType(paymentCard.getCardType())
                .transactionType(TransactionType.WITHDRAWAL)
                .build());

        return WithdrawResponse.builder()
                .iban(account.getIban())
                .charge(withdrawRequest.payOrWithdrawAmount().doubleValue())
                .additionalCharge(additionalCharge)
                .build();
    }

    public TransferResponse transfer(TransferRequest transferRequest) {

        Account fromAccount = getAccount(transferRequest.fromAccountId());
        Account toAccount = getAccount(transferRequest.toAccountId());
        PaymentCard paymentCard = getPaymentCard(transferRequest.fromAccountId(), transferRequest.paymentCartId());
        double additionalCharge = calculateAdditionalCharge(transferRequest.transferAmount().doubleValue(), paymentCard);
        double balanceAfterTransaction = calculateNewBalance(transferRequest.transferAmount().doubleValue(), fromAccount.getCurrentBalance(), additionalCharge);

        fromAccount.setCurrentBalance(balanceAfterTransaction);
        toAccount.setCurrentBalance(toAccount.getCurrentBalance() + transferRequest.transferAmount().doubleValue());
        accountService.saveMultipleAccounts(List.of(fromAccount, toAccount));

        transactionHistoryRepository.save(TransactionsHistory.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .charge(transferRequest.transferAmount().doubleValue())
                .additionalCharge(additionalCharge)
                .cardType(paymentCard.getCardType())
                .transactionType(TransactionType.TRANSFER)
                .build());

        return TransferResponse.builder()
                .fromIban(fromAccount.getIban())
                .toIban(toAccount.getIban())
                .charge(transferRequest.transferAmount().doubleValue())
                .additionalCharge(additionalCharge)
                .build();
    }

    private static double calculateNewBalance(double charge, double currentBalance, double additionalCharge) {
        double balanceAfterTransaction = currentBalance - charge - additionalCharge;
        if (balanceAfterTransaction < 0) {
            throw BankAccountHttpException.badRequest("Not enough balance");
        }
        return balanceAfterTransaction;
    }

    private static double calculateAdditionalCharge(double charge, PaymentCard paymentCard) {
        double additionalCharge = 0;
        if (paymentCard.getCardType() == CardType.CREDIT) {
            additionalCharge = charge / 100;
        }
        return additionalCharge;
    }

    private PaymentCard getPaymentCard(long accountId, Long cardId) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardService.getValidPaymentCardForAccount(accountId, cardId);
        if (optionalPaymentCard.isEmpty()) {
            log.trace("Valid card belonging to account: {} with card id: {} not found", accountId, cardId);
            throw BankAccountHttpException.badRequest("Invalid or expired card presented");
        }
        return optionalPaymentCard.get();
    }

    private Account getAccount(long accountId) {
        Optional<Account> optionalAccount = accountService.findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            log.trace("Account with id: {} not found", accountId);
            throw BankAccountHttpException.notFound("Account not found");
        }
        return optionalAccount.get();
    }
}
