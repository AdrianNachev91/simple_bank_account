package com.bank.bank_account.entities;

import com.bank.bank_account.entities.enums.CardType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "payment_card")
@Table(name = "payment_card")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCard {

    @Id
    @Column(nullable = false, name = "id")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(nullable = false, name = "expiry_date")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime expiryDate;

    @Column(nullable = false, name = "created_on")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdOn;

    @Column(nullable = false, name = "last_updated_on")
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime lastUpdatedOn;
}
