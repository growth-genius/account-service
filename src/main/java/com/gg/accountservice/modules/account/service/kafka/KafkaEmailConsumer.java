package com.gg.accountservice.modules.account.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.accountservice.modules.account.service.AccountService;
import com.gg.commonservice.annotation.BaseServiceAnnotation;
import com.gg.commonservice.dto.mail.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class KafkaEmailConsumer {

    private final ObjectMapper objectMapper;
    private final AccountService accountService;


    @KafkaListener(topics = "${kafka.user-topic.mail.send-email-fail}")
    public void sendEmailFail(String kafkaMessage) throws JsonProcessingException {
        log.error("메일 전송 실패 : {}", kafkaMessage);
        EmailMessage emailMessage = objectMapper.readValue(kafkaMessage, EmailMessage.class);
        accountService.sendEmailFail(emailMessage);
    }

}
