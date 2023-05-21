package com.sgyj.accountservice.modules.account.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.sgyj.accountservice.modules.account.dto.AccountDto;
import com.sgyj.accountservice.modules.account.dto.kafka.AccountPayload;
import com.sgyj.accountservice.modules.account.dto.kafka.Fields;
import com.sgyj.accountservice.modules.account.dto.kafka.Payload;
import com.sgyj.accountservice.modules.account.dto.kafka.Schema;
import com.sgyj.accountservice.modules.account.records.KafkaAccountDto;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAccountProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final List<Fields> fields = Arrays.asList(
        new Fields("string", true, "account_id"),
        new Fields("string", true, "username"),
        new Fields("string", true, "nickname"),
        new Fields("string", true, "email"),
        new Fields("string", true, "password"),
        new Fields("string", true, "login_type"),
        new Fields("int32", true, "age"),
        new Fields("int32", true, "birth"),
        new Fields("string", true, "account_status"),
        new Fields("string", true, "profile_image"),
        new Fields("string", true, "joined_at"),
        new Fields("int32", true, "auth_code"),
        new Fields("int32", true, "login_count"),
        new Fields("int32", true, "login_fail_count"),
        new Fields("string", true, "last_login_at"));

    private final Schema schema = Schema.builder().type("struct").fields(fields).optional(false).name("account").build();

    public void send(String kafkaTopic, AccountDto accountDto) {
        log.debug("kafkaAccount.kafkaTopic :: {}", kafkaTopic);
        Payload payload = AccountPayload.from(accountDto);
        KafkaAccountDto kafkaAccountDto = KafkaAccountDto.of(schema, payload);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        try {
            String jsonString = objectMapper.writeValueAsString(kafkaAccountDto);
            kafkaTemplate.send(kafkaTopic, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
