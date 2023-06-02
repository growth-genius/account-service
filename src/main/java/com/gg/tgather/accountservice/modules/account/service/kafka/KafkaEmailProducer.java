package com.gg.tgather.accountservice.modules.account.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.mail.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class KafkaEmailProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String kafkaTopic, EmailMessage emailMessage) throws JsonProcessingException {
        log.error("kafkaEmail.kafkaTopic :: {}", kafkaTopic);
        String message = objectMapper.writeValueAsString(emailMessage);
        kafkaTemplate.send(kafkaTopic, message);
    }

}
