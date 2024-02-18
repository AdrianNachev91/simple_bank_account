package com.bank.bank_account.web.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
@Builder
@Accessors(chain = true)
public class AccountBalance {

    Long accountId;
    String firstName;
    String lastName;
    String email;
    String iban;
    Double currentBalance;
}
