package com.bank.bank_account.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Builder
@Accessors(chain = true)
public record TransferRequest(
        @NotNull
        Long fromAccountId,

        @NotNull
        Long toAccountId,

        @NotNull
        Long paymentCartId,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal transferAmount
) {}
