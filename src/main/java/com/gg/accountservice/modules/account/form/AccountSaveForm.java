package com.gg.accountservice.modules.account.form;

import com.gg.accountservice.modules.account.enums.TravelTheme;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class AccountSaveForm {

    private String username;
    @NotNull(message = "별명을 입력해 주세요.")
    private String nickname;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    private String profileImage;

    @Min(value = 8, message = "생년월일은 8자리로 입력해 주세요. ex) 19980102")
    private int birth;

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 320, message = "유효한 이메일 주소 길이를 벗어났습니다.")
    private String email;

    private Set<TravelTheme> travelThemes;
}
