package io.github.br11.st;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.br11.st.crud.SupportTicketRepository;
import io.github.br11.st.integration.SupportTicketEventListener;
import io.github.br11.st.integration.SupportTicketIntegrationService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = { "ticket-events" }, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
})
public class SupportTicketTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SupportTicketRepository repository;

    protected final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Autowired
    protected SupportTicketIntegrationService integrationService;

    @Autowired
    protected SupportTicketEventListener eventListener;

    @BeforeAll
    protected static void setUpAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
