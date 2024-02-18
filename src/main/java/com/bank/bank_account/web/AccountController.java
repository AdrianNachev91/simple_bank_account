package com.bank.bank_account.web;

import com.bank.bank_account.services.AccountService;
import com.bank.bank_account.services.TransactionService;
import com.bank.bank_account.web.request.TransferRequest;
import com.bank.bank_account.web.request.WithdrawRequest;
import com.bank.bank_account.web.response.AccountsBalanceOverviewResponse;
import com.bank.bank_account.web.response.TransferResponse;
import com.bank.bank_account.web.response.WithdrawResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping(value = "/all/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AccountsBalanceOverviewResponse getAccountsBalanceOverview() {
        return accountService.getAccountsBalanceOverview();
    }

    @PostMapping(value = "/{accountId}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WithdrawResponse withdraw(@PathVariable(value = "accountId") long accountId,
                                     @RequestBody @Valid WithdrawRequest withdrawRequest
    ) {
        return transactionService.withdraw(accountId, withdrawRequest);
    }

    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TransferResponse transfer(@RequestBody @Valid TransferRequest transferRequest
    ) {
        return transactionService.transfer(transferRequest);
    }
}
