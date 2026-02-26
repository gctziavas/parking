package com.spacedrop.parking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"parking-events"})
@ActiveProfiles("test")
class ParkingServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
