package com.sgyj.accountservice.modules.account.dto.kafka;

import java.io.Serializable;

public record KafkaAccountDto(Schema schema, Payload payload) implements Serializable {

    public static KafkaAccountDto of(Schema schema, Payload payload) {
        return new KafkaAccountDto(schema, payload);
    }
    
}
