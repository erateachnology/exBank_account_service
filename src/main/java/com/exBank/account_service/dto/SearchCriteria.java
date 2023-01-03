package com.exBank.account_service.dto;

public record SearchCriteria(Long accountId,
                             String accountName,
                             String bankName,
                             String userNic) {
}
