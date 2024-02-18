package com.bank.bank_account.web.response;

import lombok.Builder;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
public record WithdrawResponse(
        String iban,
        Double charge,
        Double additionalCharge
) { }
