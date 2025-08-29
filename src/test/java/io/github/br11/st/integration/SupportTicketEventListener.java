package io.github.br11.st.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SupportTicketEventListener {

    private static final Logger log = LoggerFactory.getLogger(SupportTicketEventListener.class);

    private SupportTicketEvent event;

    @KafkaListener(
            topics = SupportTicketIntegrationService.TICKET_EVENTS
    )
    public void listen(
            @Payload SupportTicketEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("Received message from topic: {}, partition: {}, offset: {}", topic, partition, offset);

        this.event = event;
    }

    public Optional<SupportTicketEvent> getEvent() {
        return Optional.ofNullable(event);
    }

    public void reset() {
        event = null;
    }
}
