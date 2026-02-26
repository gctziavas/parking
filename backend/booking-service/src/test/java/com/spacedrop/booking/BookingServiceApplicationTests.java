package com.spacedrop.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"booking-events"})
@ActiveProfiles("test")
class BookingServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
