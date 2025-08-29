package io.github.br11.st.integration;

import io.github.br11.st.domain.SupportTicket;

import java.sql.Timestamp;
import java.util.UUID;

public class SupportTicketEvent {

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

    private EventType eventType;
    private UUID ticketId;
    private Timestamp timestamp;
    private SupportTicket payload;

    public SupportTicketEvent() {
    }

    public SupportTicketEvent(EventType eventType, SupportTicket payload) {
        this.eventType = eventType;
        this.ticketId = payload.getId();
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.payload = payload;
    }

    // Getters & Setters
    public EventType getEventType() {
        return eventType;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public SupportTicket getPayload() {
        return payload;
    }
}
