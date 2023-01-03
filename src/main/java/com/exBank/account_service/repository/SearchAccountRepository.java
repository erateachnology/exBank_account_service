package com.exBank.account_service.repository;

import com.exBank.account_service.dto.SearchPage;
import com.exBank.account_service.model.Account;
import com.exBank.account_service.model.User;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class SearchAccountRepository {

    public static final String NUMBER_OF_ACCOUNTS_FOUND = "Number of accounts {} found";
    public static final String SEARCH_ACCOUNTS_COMPLETED = "Search accounts {} completed";
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public SearchAccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Account> searchAccounts(SearchPage searchPage,
                                        Long accountId, String accountName,
                                        String bankName, String userNic) {

        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = criteriaQuery.from(Account.class);
        Join<Account, User> accountUserJoin = accountRoot.join("users", JoinType.INNER);

        Predicate predicate = getPredicatesForSearch(
                accountId, accountName,
                bankName, userNic, accountRoot, accountUserJoin);
        criteriaQuery.where(predicate);
        setOrder(searchPage, criteriaQuery, accountRoot);

        TypedQuery<Account> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(searchPage.getPageNumber() * searchPage.getPageSize());
        typedQuery.setMaxResults(searchPage.getPageSize());

        Pageable pageable = getPageable(searchPage);

        long accountsCount = getAccountsCount(predicate);
        log.debug(NUMBER_OF_ACCOUNTS_FOUND, accountsCount);
        log.info(SEARCH_ACCOUNTS_COMPLETED, accountName);
        return new PageImpl<>(typedQuery.getResultList(), pageable, accountsCount);
    }

    private Predicate getPredicatesForSearch(Long accountId, String accountName,
                                             String bankName, String userNic, Root<Account> accountRoot, Join<Account, User> accountUserJoin) {
        List<Predicate> predicates = new ArrayList<>();

        if (accountName != null && !accountName.isEmpty() && !accountName.trim().isEmpty()

        ) {
            predicates.add(
                    criteriaBuilder.like(accountRoot.get("accountName"), "%" + accountName + "%"));
        }

        if (accountId != null

        ) {
            predicates.add(criteriaBuilder.equal(accountRoot.get("id"), accountId));
        }

        if (bankName != null && !bankName.isEmpty() && !bankName.trim().isEmpty()

        ) {
            predicates.add(
                    criteriaBuilder.like(accountRoot.get("bankName"), "%" + accountName + "%"));
        }

        if (userNic != null && !userNic.isEmpty()
                && !userNic.trim().isEmpty()

        ) {
            predicates.add(criteriaBuilder.equal(accountUserJoin.get("nic"), userNic));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(SearchPage searchPage, CriteriaQuery<Account> criteriaQuery, Root<Account> accountRoot) {
        if (searchPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(accountRoot.get(searchPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(accountRoot.get(searchPage.getSortBy())));
        }
    }

    private Pageable getPageable(SearchPage searchPage) {
        Sort sort = Sort.by(searchPage.getSortDirection(), searchPage.getSortBy());
        return PageRequest.of(searchPage.getPageNumber(), searchPage.getPageSize(), sort);
    }

    private long getAccountsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Account> countRoot = countQuery.from(Account.class);
        countRoot.join("users");
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }


}
