package com.gg.tgather.accountservice.modules.account.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.mail.EmailMessage;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class KafkaEmailConsumer {

    private final ObjectMapper objectMapper;
    private final AccountService accountService;


    @KafkaListener(topics = "${kafka.email-topic.send-email-fail-topic}")
    public void sendEmailFail(String kafkaMessage) throws JsonProcessingException {
        log.error("메일 전송 실패 : {}", kafkaMessage);
        EmailMessage emailMessage = objectMapper.readValue(kafkaMessage, EmailMessage.class);
        accountService.sendEmailFail(emailMessage);
    }

}
