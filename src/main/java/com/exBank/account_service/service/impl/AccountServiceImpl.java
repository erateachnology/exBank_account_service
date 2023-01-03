package com.exBank.account_service.service.impl;

import com.exBank.account_service.dto.SearchPage;
import com.exBank.account_service.dto.SharedAccountRequest;
import com.exBank.account_service.model.Account;
import com.exBank.account_service.model.User;
import com.exBank.account_service.repository.AccountRepository;
import com.exBank.account_service.repository.SearchAccountRepository;
import com.exBank.account_service.service.AccountService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    public static final String FOUND_ACCOUNT_ON_ACCOUNT_ID = "Found account on account id {}";
    public static final String NOT_FOUND_ACCOUNT_ON_ACCOUNT_ID = "Not found account on account id {}";
    public static final String NO_USERS_ASSOCIATE_WITH = "No users associate with ";
    public static final String ACCOUNT = "Account";
    private final SearchAccountRepository searchAccountRepository;

    private final AccountRepository accountRepository;

    private final ModelMapper mapper;

    @Autowired
    public AccountServiceImpl(SearchAccountRepository searchAccountRepository,
                              AccountRepository accountRepository, ModelMapper mapper) {
        this.searchAccountRepository = searchAccountRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<Account> searchAccounts(Integer offset, Integer pageSize, Long accountId, String accountName,
                                        String bankName, String userNic) {
        SearchPage searchPage = SearchPage.builder()
                .pageNumber(pageSize)
                .pageNumber(offset).
                build();
        return searchAccountRepository.searchAccounts(searchPage, accountId, accountName, bankName, userNic);
    }

    @Override
    public Account createSharedAccount(SharedAccountRequest accountRequest) {
        return accountRepository.save(mapper.map(accountRequest, Account.class));
    }

    @Override
    public List<User> getAccountUsers(Long accountId) {
        Optional<Account> accountOptional =  accountRepository.findById(accountId);
        if(accountOptional.isPresent()){
            log.info(FOUND_ACCOUNT_ON_ACCOUNT_ID, accountId);
            return accountOptional.get().getUsers();
        }else{
            log.error(NOT_FOUND_ACCOUNT_ON_ACCOUNT_ID, accountId);
            throw new NotFoundException(NO_USERS_ASSOCIATE_WITH + accountId + ACCOUNT);
        }
    }
}
