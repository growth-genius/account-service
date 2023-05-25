package com.gg.accountservice.modules.account.records;

import com.gg.accountservice.modules.account.dto.kafka.Payload;
import com.gg.accountservice.modules.account.dto.kafka.Schema;
import java.io.Serializable;

public record KafkaAccountDto(Schema schema, Payload payload) implements Serializable {

    public static KafkaAccountDto of(Schema schema, Payload payload) {
        return new KafkaAccountDto(schema, payload);
    }

}
