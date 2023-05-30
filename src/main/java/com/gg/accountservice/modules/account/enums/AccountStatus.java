package com.gg.accountservice.modules.account.enums;

public enum AccountStatus {

    VERIFY_EMAIL,   // 이메일 인증 단계
    FAIL_EMAIL, // 이메일 전송 실패
    NORMAL, // 정상
    STOP,   // 중지
    SLEEP,  // 휴면
    ;

}
