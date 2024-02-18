package com.bank.bank_account.services;

import com.bank.bank_account.entities.Account;
import com.bank.bank_account.repositories.AccountRepository;
import com.bank.bank_account.web.response.AccountBalance;
import com.bank.bank_account.web.response.AccountsBalanceOverviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountsBalanceOverviewResponse getAccountsBalanceOverview() {

        List<AccountBalance> accountsBalance = new ArrayList<>();
        for (Account account : accountRepository.findAll()) {

            var accountBalance = AccountBalance.builder()
                    .accountId(account.getId())
                    .firstName(account.getFirstName())
                    .lastName(account.getLastName())
                    .email(account.getEmail())
                    .iban(account.getIban())
                    .currentBalance(account.getCurrentBalance())
                    .build();
            accountsBalance.add(accountBalance);
        }
        return AccountsBalanceOverviewResponse.builder().accountsBalanceOverview(accountsBalance).build();
    }
}
