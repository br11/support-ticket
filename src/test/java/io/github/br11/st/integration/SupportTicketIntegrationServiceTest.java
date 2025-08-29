package io.github.br11.st.integration;

import io.github.br11.st.SupportTicketTest;
import io.github.br11.st.domain.SupportTicket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.await;

class SupportTicketIntegrationServiceTest extends SupportTicketTest {

    private static final String TOPIC = "ticket-events";
    private static final String GROUP_ID = "test-group";

    @BeforeEach
    void setUp() {
        eventListener.reset();
    }

    @Test
    void create_ShouldPublishTicketCreatedEvent() {
        // Given
        SupportTicket ticket = new SupportTicket("Test Ticket created",
                "Test Description created",
                SupportTicket.Status.OPEN);
        ticket.setId(UUID.randomUUID());

        // When
        integrationService.create(ticket);

        // Then
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(SupportTicketEvent.EventType.CREATED, ticket);
        });
    }

    @Test
    void update_ShouldPublishTicketUpdatedEvent() {
        // Given
        SupportTicket ticket = new SupportTicket("Test Ticket updated",
                "Test Description updated",
                SupportTicket.Status.OPEN);
        ticket.setId(UUID.randomUUID());

        // When
        integrationService.update(ticket);

        // Then
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(SupportTicketEvent.EventType.UPDATED, ticket);
        });
    }

    @Test
    void delete_ShouldPublishTicketDeletedEvent() {
        // Given
        SupportTicket ticket = new SupportTicket("Test Ticket deleted",
                "Test Description deleted",
                SupportTicket.Status.RESOLVED);
        ticket.setId(UUID.randomUUID());

        // When
        integrationService.delete(ticket);

        // Then
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(SupportTicketEvent.EventType.DELETED, ticket);
        });
    }

    private void verify(SupportTicketEvent.EventType eventType, SupportTicket ticket) {
        Assertions.assertThat(eventListener.getEvent()).isPresent();
        SupportTicketEvent event = eventListener.getEvent().get();

        Assertions.assertThat(event.getEventType()).isEqualTo(eventType);
        Assertions.assertThat(event.getTicketId()).isEqualTo(ticket.getId());
        Assertions.assertThat(event.getPayload().getStatus()).isEqualTo(ticket.getStatus());
        Assertions.assertThat(event.getPayload().getId()).isEqualTo(ticket.getId());
        Assertions.assertThat(event.getPayload().getTitle()).isEqualTo(ticket.getTitle());
        Assertions.assertThat(event.getPayload().getDescription()).isEqualTo(ticket.getDescription());
    }
}
