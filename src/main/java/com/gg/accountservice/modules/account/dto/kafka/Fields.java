package com.gg.accountservice.modules.account.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fields {

    private String type;
    private boolean optional;
    private String field;

}
