package com.exBank.account_service.controller;

import com.exBank.account_service.dto.SharedAccountRequest;
import com.exBank.account_service.model.Account;
import com.exBank.account_service.model.User;
import com.exBank.account_service.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
public class AccountController {

    public static final String CREATE_SHARED_ACCOUNT_STARTED = "Create shared account {} started";
    public static final String GET_ALL_USERS_BY_ACCOUNT_STARTED = "Get all users by account {} started";
    public static final String SEARCH_ACCOUNT_START_FOR_ACCOUNT_ID_ACCOUNT_NAME_USER_NIC = "search account start for account id {},account name {}, userNic {}";
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Search accounts by id or name, bank name, user nic
     * @param offset
     * @param pageSize
     * @param accountId
     * @param accountName
     * @param bankName
     * @param userNic
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Page<Account>> searchAccounts(@RequestParam("offset") Integer offset,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("accountId") Long accountId,
                                                        @RequestParam("accountName") String accountName,
                                                        @RequestParam("bankName") String bankName,
                                                        @RequestParam("userNic") String userNic){
        log.info(SEARCH_ACCOUNT_START_FOR_ACCOUNT_ID_ACCOUNT_NAME_USER_NIC, accountId, accountName, userNic);
        return ResponseEntity.ok(accountService.searchAccounts(offset, pageSize, accountId,accountName,bankName,userNic ));
    }

    /**
     * Save shared account
     * @param accountRequest
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Account> createSharedAccount(@RequestBody SharedAccountRequest accountRequest){
        log.info(CREATE_SHARED_ACCOUNT_STARTED, accountRequest.accountName());
        return new ResponseEntity<>(accountService.createSharedAccount(accountRequest), HttpStatus.CREATED);
    }

    /**
     * Get all users associated to account
     * @param accountId
     * @return
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<List<User>> getAccountUsers(@PathVariable(name = "accountId") Long accountId){
        log.info(GET_ALL_USERS_BY_ACCOUNT_STARTED, accountId);
        return ResponseEntity.ok(accountService.getAccountUsers(accountId));
    }

}
