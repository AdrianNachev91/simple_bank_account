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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private AccountService accountService;
    @Mock
    private PaymentCardService paymentCardService;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(accountService, paymentCardService, transactionHistoryRepository);
    }

    @DisplayName("Transfer with credit card")
    @Test
    void transferWithCredit() {

        // Prepare
        var optionalFromAccount = Optional.of(Account.builder().id(1L).currentBalance(200.0).iban("iban").build());
        when(accountService.findAccountById(1)).thenReturn(optionalFromAccount);
        var optionalToAccount = Optional.of(Account.builder().id(2L).currentBalance(200.0).iban("iban2").build());
        when(accountService.findAccountById(2)).thenReturn(optionalToAccount);
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.CREDIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .paymentCartId(1L)
                .transferAmount(new BigDecimal("100"))
                .build();

        // Test
        TransferResponse result = transactionService.transfer(request);

        // Verify
        var transactionHistory = TransactionsHistory.builder()
                .fromAccount(optionalFromAccount.get())
                .toAccount(optionalToAccount.get())
                .cardType(CardType.CREDIT)
                .transactionType(TransactionType.TRANSFER)
                .charge(100.0)
                .additionalCharge(1.0)
                .build();

        assertThat(result.fromIban()).isEqualTo("iban");
        assertThat(result.toIban()).isEqualTo("iban2");
        assertThat(result.charge()).isEqualTo(100);
        assertThat(result.additionalCharge()).isEqualTo(1);
        assertThat(optionalFromAccount.get().getCurrentBalance()).isEqualTo(99);
        assertThat(optionalToAccount.get().getCurrentBalance()).isEqualTo(300);

        verify(accountService).saveMultipleAccounts(List.of(optionalFromAccount.get(), optionalToAccount.get()));
        verify(transactionHistoryRepository).save(transactionHistory);
    }

    @DisplayName("Transfer with debit card")
    @Test
    void transferWithDebit() {

        // Prepare
        var optionalFromAccount = Optional.of(Account.builder().id(1L).currentBalance(200.0).iban("iban").build());
        when(accountService.findAccountById(1)).thenReturn(optionalFromAccount);
        var optionalToAccount = Optional.of(Account.builder().id(2L).currentBalance(200.0).iban("iban2").build());
        when(accountService.findAccountById(2)).thenReturn(optionalToAccount);
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.DEBIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .paymentCartId(1L)
                .transferAmount(new BigDecimal("100"))
                .build();

        // Test
        TransferResponse result = transactionService.transfer(request);

        // Verify
        var transactionHistory = TransactionsHistory.builder()
                .fromAccount(optionalFromAccount.get())
                .toAccount(optionalToAccount.get())
                .cardType(CardType.DEBIT)
                .transactionType(TransactionType.TRANSFER)
                .charge(100.0)
                .additionalCharge(.0)
                .build();

        assertThat(result.fromIban()).isEqualTo("iban");
        assertThat(result.toIban()).isEqualTo("iban2");
        assertThat(result.charge()).isEqualTo(100);
        assertThat(result.additionalCharge()).isEqualTo(0);
        assertThat(optionalFromAccount.get().getCurrentBalance()).isEqualTo(100);
        assertThat(optionalToAccount.get().getCurrentBalance()).isEqualTo(300);

        verify(accountService).saveMultipleAccounts(List.of(optionalFromAccount.get(), optionalToAccount.get()));
        verify(transactionHistoryRepository).save(transactionHistory);
    }

    @DisplayName("Try to transfer with non existing account")
    @Test
    void transferNoFromAccountFound() {

        // Prepare
        when(accountService.findAccountById(1)).thenReturn(Optional.empty());

        // Test
        Throwable e = catchThrowable(() -> transactionService.transfer(TransferRequest.builder().fromAccountId(1L).build()));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Account not found");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @DisplayName("Try to transfer to non existing account")
    @Test
    void transferNoToAccountFound() {

        // Prepare
        when(accountService.findAccountById(1)).thenReturn(Optional.of(Account.builder().build()));
        when(accountService.findAccountById(2)).thenReturn(Optional.empty());

        var request = TransferRequest.builder().fromAccountId(1L).toAccountId(2L).build();

        // Test
        Throwable e = catchThrowable(() -> transactionService.transfer(request));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Account not found");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @DisplayName("Try to transfer with non valid card")
    @Test
    void transferNoValidCardFound() {

        // Prepare
        when(accountService.findAccountById(1)).thenReturn(Optional.of(Account.builder().build()));
        when(accountService.findAccountById(2)).thenReturn(Optional.of(Account.builder().build()));
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(Optional.empty());

        var request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .paymentCartId(1L)
                .build();

        // Test
        Throwable e = catchThrowable(() -> transactionService.transfer(request));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Invalid or expired card presented");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @DisplayName("Try to transfer without enough balance")
    @Test
    void transferNotEnoughBalance() {

        // Prepare
        var optionalAccount = Optional.of(Account.builder().currentBalance(10.55).build());
        when(accountService.findAccountById(1)).thenReturn(optionalAccount);
        when(accountService.findAccountById(2)).thenReturn(Optional.of(Account.builder().build()));
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.DEBIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .paymentCartId(1L)
                .transferAmount(new BigDecimal("10.56"))
                .build();

        // Test
        Throwable e = catchThrowable(() -> transactionService.transfer(request));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Not enough balance");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @DisplayName("Withdraw with credit card")
    @Test
    void withdrawWithCredit() {

        // Prepare
        var optionalAccount = Optional.of(Account.builder().id(1L).currentBalance(200.0).iban("iban").build());
        when(accountService.findAccountById(1)).thenReturn(optionalAccount);
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.CREDIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = WithdrawRequest.builder()
                .paymentCartId(1L)
                .payOrWithdrawAmount(new BigDecimal("100"))
                .build();

        // Test
        WithdrawResponse result = transactionService.withdraw(1, request);

        // Verify
        var transactionHistory = TransactionsHistory.builder()
                .fromAccount(optionalAccount.get())
                .cardType(CardType.CREDIT)
                .transactionType(TransactionType.WITHDRAWAL)
                .charge(100.0)
                .additionalCharge(1.0)
                .build();

        assertThat(result.iban()).isEqualTo("iban");
        assertThat(result.charge()).isEqualTo(100);
        assertThat(result.additionalCharge()).isEqualTo(1);
        assertThat(optionalAccount.get().getCurrentBalance()).isEqualTo(99);

        verify(accountService).saveAccount(optionalAccount.get());
        verify(transactionHistoryRepository).save(transactionHistory);
    }

    @DisplayName("Withdraw with debit card")
    @Test
    void withdrawWithDebit() {

        // Prepare
        var optionalAccount = Optional.of(Account.builder().id(1L).currentBalance(10.55).iban("iban").build());
        when(accountService.findAccountById(1)).thenReturn(optionalAccount);
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.DEBIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = WithdrawRequest.builder()
                .paymentCartId(1L)
                .payOrWithdrawAmount(new BigDecimal("10.55"))
                .build();

        // Test
        WithdrawResponse result = transactionService.withdraw(1, request);

        // Verify
        var transactionHistory = TransactionsHistory.builder()
                .fromAccount(optionalAccount.get())
                .cardType(CardType.DEBIT)
                .transactionType(TransactionType.WITHDRAWAL)
                .charge(10.55)
                .additionalCharge(.0)
                .build();

        assertThat(result.iban()).isEqualTo("iban");
        assertThat(result.charge()).isEqualTo(10.55);
        assertThat(result.additionalCharge()).isEqualTo(0);
        assertThat(optionalAccount.get().getCurrentBalance()).isEqualTo(0);

        verify(accountService).saveAccount(optionalAccount.get());
        verify(transactionHistoryRepository).save(transactionHistory);
    }

    @DisplayName("Try to withdraw with non existing account")
    @Test
    void withdrawNoAccountFound() {

        // Prepare
        when(accountService.findAccountById(1)).thenReturn(Optional.empty());

        // Test
        Throwable e = catchThrowable(() -> transactionService.withdraw(1, WithdrawRequest.builder().build()));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Account not found");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @DisplayName("Try to withdraw with non valid card")
    @Test
    void withdrawNoValidCardFound() {

        // Prepare
        when(accountService.findAccountById(1)).thenReturn(Optional.of(Account.builder().build()));
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(Optional.empty());

        var request = WithdrawRequest.builder().paymentCartId(1L).build();

        // Test
        Throwable e = catchThrowable(() -> transactionService.withdraw(1, request));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Invalid or expired card presented");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @DisplayName("Try to withdraw without enough balance")
    @Test
    void withdrawNotEnoughBalance() {

        // Prepare
        var optionalAccount = Optional.of(Account.builder().currentBalance(10.55).build());
        when(accountService.findAccountById(1)).thenReturn(optionalAccount);
        var optionalCard = Optional.of(PaymentCard.builder().cardType(CardType.DEBIT).build());
        when(paymentCardService.getValidPaymentCardForAccount(1, 1)).thenReturn(optionalCard);

        var request = WithdrawRequest.builder()
                .paymentCartId(1L)
                .payOrWithdrawAmount(new BigDecimal("10.56"))
                .build();

        // Test
        Throwable e = catchThrowable(() -> transactionService.withdraw(1, request));

        // Verify
        assertThat(e).isInstanceOf(BankAccountHttpException.class);
        assertThat(e.getMessage()).isEqualTo("Not enough balance");
        assertThat(((BankAccountHttpException)e).getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}