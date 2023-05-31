package com.gg.accountservice.modules.account.repository;

import com.gg.accountservice.modules.account.entity.Account;
import com.gg.commonservice.enums.LoginType;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNickname(String nickname);

    Optional<Account> findByEmailAndLoginType(String email, LoginType loginType);

    Optional<Account> findByEmail(String email);

    /**
     * 사용자 정보 조회
     *
     * @param accountId : 사용자 식별자
     * @return optional 객체
     */
    @EntityGraph("Account.withRolesAndTravelThemes")
    Optional<Account> findByAccountId(String accountId);

}
