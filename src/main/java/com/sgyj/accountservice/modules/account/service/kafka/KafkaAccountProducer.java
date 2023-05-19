package com.sgyj.accountservice.modules.account.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.sgyj.accountservice.modules.account.dto.AccountDto;
import com.sgyj.accountservice.modules.account.dto.kafka.AccountPayload;
import com.sgyj.accountservice.modules.account.dto.kafka.Fields;
import com.sgyj.accountservice.modules.account.dto.kafka.KafkaAccountDto;
import com.sgyj.accountservice.modules.account.dto.kafka.Payload;
import com.sgyj.accountservice.modules.account.dto.kafka.Schema;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAccountProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final List<Fields> fields = Arrays.asList(
        new Fields("string", true, "uuid"),
        new Fields("string", true, "username"),
        new Fields("string", true, "nickname"),
        new Fields("string", true, "email"),
        new Fields("string", true, "password"),
        new Fields("string", true, "loginType"),
        new Fields("int32", true, "age"),
        new Fields("int32", true, "birth"),
        new Fields("string", true, "accountStatus"),
        new Fields("string", true, "profileImage"),
        new Fields("datetime", true, "joinedAt"),
        new Fields("int32", true, "authCode"),
        new Fields("int32", true, "loginCount"),
        new Fields("int32", true, "loginFailCount"),
        new Fields("datetime", true, "lastLoginAt"));

    private final Schema schema = Schema.builder().type("struct").fields(fields).optional(false).name("account").build();

    public void send(String kafkaTopic, AccountDto accountDto) {
        log.debug("kafkaAccount.kafkaTopic :: {}", kafkaTopic);
        Payload payload = AccountPayload.from(accountDto);
        KafkaAccountDto kafkaAccountDto = KafkaAccountDto.of(schema, payload);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        try {
            String jsonString = objectMapper.writeValueAsString(kafkaAccountDto);
            SendResult<String, String> stringStringSendResult = kafkaTemplate.send(kafkaTopic, jsonString).get();
            ProducerRecord<String, String> producerRecord = stringStringSendResult.getProducerRecord();
            log.info("key :: {}", producerRecord.key());
            log.info("key :: {}", producerRecord.partition());
            log.info("account Producer send data from the Account microservice : " + kafkaAccountDto);
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
