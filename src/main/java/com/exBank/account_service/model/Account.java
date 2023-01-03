package com.exBank.account_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Account")
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {

    //TODO this should be 7-digit number or 9-digit number
    @Id
    @Column(name = "id")
    private Long id;

    private String accountName;
    //TODO This should be type of Enum, FD, SAVING,RFC
    private String accountType;

    //TODO replace with Embeddable class
    private BigDecimal balance;
    private Long transactionId;
    private String bankName;
    private String bankBranchCode;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users =  new ArrayList<>();
}
