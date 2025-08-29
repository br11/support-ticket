package io.github.br11.st.integration;

import io.github.br11.st.domain.SupportTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SupportTicketIntegrationService {

    public static final String TICKET_EVENTS = "ticket-events";

    private final KafkaTemplate<String, SupportTicketEvent> kafkaTemplate;

    @Autowired
    public SupportTicketIntegrationService(KafkaTemplate<String, SupportTicketEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(SupportTicket ticket) {
        try {
            kafkaTemplate.send(TICKET_EVENTS,
                    ticket.getId().toString(),
                    toEvent(SupportTicketEvent.EventType.CREATED, ticket)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(SupportTicket ticket) {
        kafkaTemplate.send(TICKET_EVENTS,
                ticket.getId().toString(),
                toEvent(SupportTicketEvent.EventType.UPDATED, ticket));
    }

    public void delete(SupportTicket ticket) {
        kafkaTemplate.send(TICKET_EVENTS,
                ticket.getId().toString(),
                toEvent(SupportTicketEvent.EventType.DELETED, ticket));
    }

    private SupportTicketEvent toEvent(SupportTicketEvent.EventType eventType,
                                       SupportTicket ticket) {
        return new SupportTicketEvent(eventType, ticket);
    }
}
