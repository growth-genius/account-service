package com.gg.tgather.accountservice.modules.account.entity;

import com.gg.tgather.accountservice.modules.account.enums.AccountStatus;
import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.ModifyAccountForm;
import com.gg.tgather.commonservice.advice.exceptions.BadRequestException;
import com.gg.tgather.commonservice.enums.AccountRole;
import com.gg.tgather.commonservice.enums.ErrorMessage;
import com.gg.tgather.commonservice.enums.LoginType;
import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.jpa.UpdatedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "Account.withRolesAndTravelThemes", attributeNodes = {@NamedAttributeNode("roles"), @NamedAttributeNode("travelThemes")})
public class Account extends UpdatedEntity {

    /* 아이디 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* 고유 식별자 */
    @Column(unique = true)
    private String accountId;
    /* 사용자 이름 */
    private String username;
    /* 사용자 별명 */
    private String nickname;
    /* 이메일 */
    private String email;
    /* 비밀번호 */
    private String password;
    /* 로그인 형태 */
    @Enumerated(EnumType.STRING)
    private LoginType loginType;
    /* 권한 */
    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    private Set<AccountRole> roles = Set.of(AccountRole.USER);
    /* 나이 */
    private int age;

    private int birth;
    /* 인증코드 */
    private String authCode;

    private LocalDateTime authCodeModifiedAt;

    /* 여행 테마 */
    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "travel_themes_account", joinColumns = @JoinColumn(name = "account_id"))
    private Set<TravelTheme> travelThemes;

    /* 계정 상태 */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.VERIFY_EMAIL;

    /* 프로필 이미지 */
    @Lob
    @Basic
    private String profileImage;

    /* 가입일자 */
    private LocalDateTime joinedAt;

    /* 로그인 횟수 */
    private int loginCount;

    /* 로그인 실패 회수 */
    private int loginFailCount;

    /* 마지막 로그인 일자 */
    private LocalDateTime lastLoginAt;

    /* fcm 토큰 */
    private String fcmToken;

    /* 로그인 후 세팅 */
    public void afterLoginSuccess(String fcmToken) {
        this.loginFailCount = 0;
        this.loginCount++;
        this.lastLoginAt = LocalDateTime.now();
        if (StringUtils.isNotEmpty(fcmToken)) this.changeFcmTokenIfChanged(fcmToken);
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String password) {
        this.password = password;
    }

    /**
     * 프로필 사진 변경
     */
    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * 이름 변경
     */
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void login(PasswordEncoder passwordEncoder, String credential) {
        if (!passwordEncoder.matches(credential, this.password)) {
            this.loginFailCount++;
            throw new BadRequestException(ErrorMessage.NOT_MATCHED_ACCOUNT.getMessage());
        } else if (this.accountStatus != AccountStatus.NORMAL) {
            throw new BadRequestException(ErrorMessage.VERIFY_EMAIL.getMessage());
        }
    }

    public void successAuthUser() {
        this.accountStatus = AccountStatus.NORMAL;
    }

    /**
     * 입력 데이터로 Account 계정 생성
     *
     * @param accountSaveForm Account 입력 form
     * @return 계정 entity
     */
    public static Account createAccount(AccountSaveForm accountSaveForm) {
        Account account = new Account();
        account.accountId = UUID.randomUUID().toString();
        account.username = accountSaveForm.getUsername();
        account.password = accountSaveForm.getPassword();
        account.email = accountSaveForm.getEmail();
        account.birth = accountSaveForm.getBirth();
        account.nickname = accountSaveForm.getNickname();
        account.profileImage = accountSaveForm.getProfileImage();
        account.travelThemes = accountSaveForm.getTravelThemes();
        account.loginType = LoginType.TGAHTER;
        account.joinedAt = LocalDateTime.now();
        account.authCodeModifiedAt = LocalDateTime.now();
        return account;
    }

    public void updateAuthCode(String authCode) {
        this.accountStatus = AccountStatus.VERIFY_EMAIL;
        this.authCode = authCode;
    }

    public void modifyAccountInfo(ModifyAccountForm modifyAccountForm) {
        this.nickname = modifyAccountForm.getNickname();
        this.travelThemes = modifyAccountForm.getTravelThemes();
        this.username = modifyAccountForm.getUsername();
        this.profileImage = modifyAccountForm.getProfileImage();
    }

    /**
     * fcm 토큰 변경 시 토큰 변경
     *
     * @param fcmToken : fcm 토큰
     */
    public void changeFcmTokenIfChanged(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
