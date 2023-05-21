package com.sgyj.accountservice.modules.account.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.sgyj.accountservice.infra.security.Jwt;
import com.sgyj.accountservice.modules.account.entity.Account;
import com.sgyj.accountservice.modules.account.enums.AccountRole;
import com.sgyj.accountservice.modules.account.enums.LoginType;
import com.sgyj.accountservice.modules.account.enums.TravelTheme;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class AccountDto {

    /** 로그인 아이디 */
    private Long id;
    /** 고유 식별자 */
    private String accountId;
    /** 이메일 */
    private String email;
    /** 사용자 이름 */
    private String userName;
    /* 사용자 별명 */
    private String nickname;
    /* 패스워드*/
    private String password;
    /** 가입일자 */
    private LocalDateTime joinedAt;
    /** 권한 */
    private Set<AccountRole> roles = Set.of( AccountRole.USER );
    /** 로그인 횟수 */
    private int loginCount;
    /** 마지막 로그인 일자 */
    private LocalDateTime lastLoginAt;
    /** 프로필 이미지 */
    private String profileImage;
    /** 로그인 타입 */
    private LoginType loginType;
    /** 접속 토큰 */
    private String accessToken;
    /** 재발급 토큰 */
    private String refreshToken;
    /** 나이 */
    private int age;
    /** 생년월 */
    private int birth;
    /** 여행 테마 */
    private Set<TravelTheme> travelThemes;

    private AccountDto ( Account account ) {
        copyProperties(account, this);
    }

    public static AccountDto from ( Account account ) {
        return new AccountDto( account );
    }

    public static AccountDto createByAccountAndGenerateAccessToken ( Account account, Jwt jwt ) {
        AccountDto accountDTO = new AccountDto( account );
        accountDTO.generateAccessToken( jwt );
        return accountDTO;
    }

    public void generateAccessToken ( Jwt jwt ) {
        Jwt.Claims claims = Jwt.Claims.of(id, accountId, email, roles.stream().map( AccountRole::name ).toArray( String[]::new ) );
        this.accessToken = jwt.createAccessToken( claims );
        this.refreshToken = jwt.createRefreshToken( claims );
    }
}
