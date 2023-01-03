package com.exBank.account_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record SharedAccountRequest(String accountName,
                                   String accountType,
                                   BigDecimal balance,
                                   Long transactionId,
                                   String bankName,
                                   String bankBranchCode,
                                   List<UserRequest> user) {
}
