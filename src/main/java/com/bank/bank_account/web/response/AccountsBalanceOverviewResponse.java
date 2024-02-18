package com.bank.bank_account.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
@Builder
@Accessors(chain = true)
public class AccountsBalanceOverviewResponse {

    List<AccountBalance> accountsBalanceOverview;
}
