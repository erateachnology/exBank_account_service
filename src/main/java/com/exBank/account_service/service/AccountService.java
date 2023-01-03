package com.exBank.account_service.service;

import com.exBank.account_service.dto.SharedAccountRequest;
import com.exBank.account_service.model.Account;
import com.exBank.account_service.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {
    Page<Account> searchAccounts(Integer offset, Integer pageSize, Long accountId,String accountName,
                                 String bankName, String userNic);

    Account createSharedAccount(SharedAccountRequest accountRequest);

    List<User> getAccountUsers(Long accountId);
}
