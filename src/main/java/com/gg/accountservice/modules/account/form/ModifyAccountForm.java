package com.gg.accountservice.modules.account.form;

import com.gg.accountservice.modules.account.enums.TravelTheme;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class ModifyAccountForm {

    private String username;
    
    @NotNull(message = "별명을 입력해 주세요.")
    private String nickname;

    private String profileImage;

    private Set<TravelTheme> travelThemes;

}
