package com.spacedrop.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
@ActiveProfiles("test")
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
