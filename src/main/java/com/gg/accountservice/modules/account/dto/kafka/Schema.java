package com.gg.accountservice.modules.account.dto.kafka;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Schema {

    private String type;
    private List<Fields> fields;

    private boolean optional;

    private String name;

}
