package io.github.br11.st.crud.dto;

import io.github.br11.st.domain.SupportTicket;
import org.springframework.validation.annotation.Validated;

public class SupportTicketDto {

    private String title;
    private String description;
    private SupportTicket.Status status;

    public SupportTicketDto() {
    }

    public SupportTicketDto(String title, String description, SupportTicket.Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupportTicket.Status getStatus() {
        return status;
    }

    public void setStatus(SupportTicket.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SupportTicket{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
