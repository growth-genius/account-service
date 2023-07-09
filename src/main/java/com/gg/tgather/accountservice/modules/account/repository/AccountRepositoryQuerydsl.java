package com.gg.tgather.accountservice.modules.account.repository;

import com.gg.tgather.accountservice.modules.account.dto.CustomAccountDto;
import java.util.List;

public interface AccountRepositoryQuerydsl {

    List<CustomAccountDto> findAllByAccountIds(List<String> accountIds);

}
