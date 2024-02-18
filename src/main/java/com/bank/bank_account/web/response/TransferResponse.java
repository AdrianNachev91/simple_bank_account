package com.bank.bank_account.web.response;

import lombok.Builder;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
public record TransferResponse(
        String fromIban,
        String toIban,
        Double charge,
        Double additionalCharge
) { }
