package com.bank.bank_account.entities;

import com.bank.bank_account.entities.enums.CardType;
import com.bank.bank_account.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;

@Entity(name = "transaction_history")
@Table(name = "transaction_history")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionsHistory {

    @Id
    @Column(nullable = false, name = "id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(nullable = false, name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(nullable = false, name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false, name = "charge", columnDefinition = "DECIMAL(10,5)")
    private Double charge;

    @Column(nullable = false, name = "extra_charge", columnDefinition = "DECIMAL(10,5) default 0")
    private Double extraCharge;

    @Column(nullable = false, name = "created_on")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdOn;


}
