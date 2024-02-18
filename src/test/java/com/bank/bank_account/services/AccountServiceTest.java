package com.bank.bank_account.services;

import com.bank.bank_account.entities.Account;
import com.bank.bank_account.repositories.AccountRepository;
import com.bank.bank_account.utils.TestDataUtils;
import com.bank.bank_account.web.response.AccountBalance;
import com.bank.bank_account.web.response.AccountsBalanceOverviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @DisplayName("Get all accounts balance overview")
    @Test
    void getAccountsBalanceOverview() {

        // Prepare
        List<Account> accounts = TestDataUtils.getInitialAccountsData();

        when(accountRepository.findAll()).thenReturn(accounts);

        // Test
        AccountsBalanceOverviewResponse result = accountService.getAccountsBalanceOverview();

        // Verify
        assertThat(result).isNotNull();
        List<AccountBalance> overviewList = result.getAccountsBalanceOverview();
        assertThat(overviewList).hasSize(3);
        assertThat(overviewList.get(0).getAccountId()).isEqualTo(1);
        assertThat(overviewList.get(0).getCurrentBalance()).isEqualTo(453.43);
        assertThat(overviewList.get(1).getAccountId()).isEqualTo(2);
        assertThat(overviewList.get(1).getCurrentBalance()).isEqualTo(1232.12);
        assertThat(overviewList.get(2).getAccountId()).isEqualTo(3);
        assertThat(overviewList.get(2).getCurrentBalance()).isEqualTo(43.21);
    }

    @DisplayName("Save account")
    @Test
    void saveAccount() {

        // Prepare
        Account account = Account.builder().id(1L).build();

        // Test
        accountService.saveAccount(account);

        // Verify
        verify(accountRepository).save(account);
    }
}