package com.gg.tgather.accountservice.modules.account.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gg.tgather.accountservice.modules.account.config.KafkaTestConfig;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.enums.AccountStatus;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.accountservice.modules.account.service.kafka.KafkaEmailConsumer;
import com.gg.tgather.accountservice.modules.account.service.kafka.KafkaEmailProducer;
import com.gg.tgather.commonservice.dto.mail.EmailMessage;
import com.gg.tgather.commonservice.dto.mail.MailSubject;
import com.gg.tgather.commonservice.properties.KafkaUserTopicProperties;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@Slf4j
@DirtiesContext
@SpringBootTest
@Import(KafkaTestConfig.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private KafkaEmailConsumer kafkaEmailConsumer;

    @Autowired
    private KafkaEmailProducer kafkaEmailProducer;

    @Autowired
    private KafkaUserTopicProperties kafkaUserTopicProperties;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("인증코드 전송 실패시 상태변경")
    void test_case_1() throws InterruptedException, JsonProcessingException {
        // given
        String mail = "leesg107@naver.com";
        EmailMessage emailMessage = EmailMessage.builder().accountId("QJEQRJQEROJGEQ").to(mail).mailSubject(MailSubject.VALID_AUTHENTICATION_ACCOUNT)
            .message("authcode_").build();
        log.error("emailMessage :: {}", emailMessage.toString());
        // when
        kafkaEmailProducer.send(kafkaUserTopicProperties.getAuthenticationMailTopic(), emailMessage);
        boolean messageConsumed = kafkaEmailConsumer.getLatch().await(10, TimeUnit.SECONDS);
        // then
        assertTrue(messageConsumed);
        Account account = accountRepository.findByEmail(mail).orElseThrow();
        assertEquals(AccountStatus.FAIL_EMAIL, account.getAccountStatus());
    }


}
