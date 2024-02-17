package com.bank.bank_account.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "account")
@Table(name = "account")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Id
    @Column(nullable = false, name = "id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "created_on")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdOn;

    @Column(nullable = false, name = "last_updated_on")
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime lastUpdatedOn;
}
