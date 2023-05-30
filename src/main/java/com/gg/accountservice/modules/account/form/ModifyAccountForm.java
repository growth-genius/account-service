package com.gg.accountservice.modules.account.form;

import com.gg.accountservice.modules.account.enums.TravelTheme;
import java.util.Set;
import lombok.Data;

@Data
public class ModifyAccountForm {

    private String username;

    private String nickname;

    private String profileImage;

    private Set<TravelTheme> travelThemes;

}
