package com.gg.tgather.accountservice.modules.account.repository;

import static com.gg.tgather.accountservice.modules.account.entity.QAccount.account;
import static com.querydsl.core.types.Projections.constructor;

import com.gg.tgather.accountservice.modules.account.dto.CustomAccountDto;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.commonservice.jpa.Querydsl5Support;
import java.util.List;

public class AccountRepositoryImpl extends Querydsl5Support implements AccountRepositoryQuerydsl {

    protected AccountRepositoryImpl() {
        super(Account.class);
    }

    @Override
    public List<CustomAccountDto> findAllByAccountIds(List<String> accountIds) {
        return select(constructor(CustomAccountDto.class, account)).from(account).where(account.accountId.in(accountIds)).fetch();
    }

}
