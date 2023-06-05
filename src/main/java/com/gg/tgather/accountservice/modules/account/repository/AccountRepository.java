package com.gg.tgather.accountservice.modules.account.repository;

import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.commonservice.enums.LoginType;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * account 관련 인터페이스 저장소
 *
 * @author joyeji
 * @since 2023.06.05
 */
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 닉네임 유효성 확인
     *
     * @param nickname 닉네임
     * @return optional : 유효한 닉네임 확인 결과값
     */
    Optional<Account> findByNickname(String nickname);

    /**
     * 이메일과 로그인 유형이 일치하는 사용자 확인
     *
     * @param email     : 이메일
     * @param loginType : 로그인 유형
     * @return optional : 유효한 사용자 확인 결과값
     */
    @EntityGraph("Account.withRolesAndTravelThemes")
    Optional<Account> findByEmailAndLoginType(String email, LoginType loginType);

    /**
     * 이메일 조회
     *
     * @param email : 이메일 주소
     * @return optional : 유효한 이메일 확인 결과값
     */
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
