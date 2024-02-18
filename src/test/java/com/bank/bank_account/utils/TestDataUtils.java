package com.bank.bank_account.utils;

import com.bank.bank_account.entities.Account;
import com.bank.bank_account.entities.PaymentCard;

import java.util.List;

public class TestDataUtils {

    public static List<Account> getInitialAccountsData() {

        return List.of(
                Account.builder()
                        .id(1L)
                        .firstName("Adrian")
                        .lastName("Nachev")
                        .email("adrian@example.com")
                        .iban("NL63RABO3665292913")
                        .currentBalance(453.43)
                        .build(),
                Account.builder()
                        .id(2L)
                        .firstName("Suzan")
                        .lastName("Hart")
                        .email("suzan@example.com")
                        .iban("NL49RABO5350244469")
                        .currentBalance(1232.12)
                        .build(),
                Account.builder()
                        .id(3L)
                        .firstName("Peter")
                        .lastName("Jhones")
                        .email("peter@example.com")
                        .iban("NL72RABO8727958558")
                        .currentBalance(43.21)
                        .build()
        );
    }
}
