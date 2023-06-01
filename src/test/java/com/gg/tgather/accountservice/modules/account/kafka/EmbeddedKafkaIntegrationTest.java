package com.gg.tgather.accountservice.modules.account.kafka;

import com.gg.tgather.accountservice.modules.account.config.KafkaTestConfig;
import com.gg.tgather.accountservice.modules.account.service.kafka.KafkaEmailConsumer;
import com.gg.tgather.accountservice.modules.account.service.kafka.KafkaEmailProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest
@Import(KafkaTestConfig.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private KafkaEmailConsumer kafkaEmailConsumer;

    @Autowired
    private KafkaEmailProducer kafkaEmailProducer;

    @Test
    @DisplayName("계정 인증용 메일 전송")
    void test_case_1() {
        System.out.println("test");
    }

}
