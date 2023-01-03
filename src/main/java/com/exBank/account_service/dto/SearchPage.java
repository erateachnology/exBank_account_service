package com.exBank.account_service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@Builder(toBuilder = true)
public class SearchPage {
    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "accountName";
}
