package io.github.br11.st.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.br11.st.SupportTicketTest;
import io.github.br11.st.crud.dto.SupportTicketDto;
import io.github.br11.st.domain.SupportTicket;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest  // boots up the full application context
//@AutoConfigureMockMvc
//@EmbeddedKafka(partitions = 1, topics = { "ticket-events" }, brokerProperties = {
//        "listeners=PLAINTEXT://localhost:9092",
//        "port=9092"
//})
class SupportTicketControllerTest  extends SupportTicketTest {

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void createSupportTicket_ShouldReturnCreatedTicket() throws Exception {
        // Given
        SupportTicketDto ticketDto = newTicketDto();

        // When
        String json = mockMvc.perform(post("/api/support-tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDto)))
        // Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is(ticketDto.getTitle())))
                .andExpect(jsonPath("$.description", is(ticketDto.getDescription())))
                .andExpect(jsonPath("$.status", is(ticketDto.getStatus().toString())))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();


        UUID id = UUID.fromString(objectMapper.readTree(json).path("id").asText());
        Assertions.assertThat(repository.findById(id)).isPresent();
    }

    @Test
    @Transactional
    void getSupportTicket_WhenExists_ShouldReturnTicket() throws Exception {
        // Given
        SupportTicket ticket = insertData();

        String createdAt = toIsoTimestamp(ticket.getCreatedAt());
        String updatedAt = toIsoTimestamp(ticket.getUpdatedAt());

        // When
        mockMvc.perform(get("/api/support-tickets/" + ticket.getId()))
        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ticket.getId().toString())))
                .andExpect(jsonPath("$.title", is(ticket.getTitle())))
                .andExpect(jsonPath("$.description", is(ticket.getDescription())))
                .andExpect(jsonPath("$.status", is(ticket.getStatus().toString())))
                .andExpect(jsonPath("$.createdAt", is(createdAt)))
                .andExpect(jsonPath("$.updatedAt", is(updatedAt)));
   }

   @Test
   void getAllSupportTickets_ShouldReturnAllTickets() throws Exception {
        // Given
        insertData();
        insertData();
        insertData();
        // When
        mockMvc.perform(get("/api/support-tickets"))
        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void updateSupportTicket_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        SupportTicketDto ticketDto = newTicketDto();

        // When
        mockMvc.perform(put("/api/support-tickets/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDto)))
        // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSupportTicket_WhenExists_ShouldReturnUpdatedTicket() throws Exception {
        // Given
        SupportTicket ticket = insertData();
        String createdAt = toIsoTimestamp(ticket.getCreatedAt());
        String updatedAt = toIsoTimestamp(ticket.getUpdatedAt());

        SupportTicketDto ticketDto = newTicketDto();
        ticketDto.setDescription("Updated Description");
        ticketDto.setStatus(SupportTicket.Status.IN_PROGRESS);
        ticketDto.setTitle("Updated Title");

        // When
        mockMvc.perform(put("/api/support-tickets/" + ticket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDto)))
       // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ticket.getId().toString())))
                .andExpect(jsonPath("$.title", is(ticketDto.getTitle())))
                .andExpect(jsonPath("$.description", is(ticketDto.getDescription())))
                .andExpect(jsonPath("$.status", is(ticketDto.getStatus().toString())))
                .andExpect(jsonPath("$.createdAt", is(createdAt)))
                .andExpect(jsonPath("$.updatedAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", not(updatedAt)));
    }

    @Test
    void deleteSupportTicket_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        SupportTicket ticket = insertData();

        // When
        mockMvc.perform(delete("/api/support-tickets/" + ticket.getId()))
        // Then
                .andExpect(status().isNoContent());

    }

    @Test
    void getSupportTicket_WhenNotExists_ShouldReturnNoContent() throws Exception {
        // When
        mockMvc.perform(get("/api/support-tickets/" + UUID.randomUUID()))
        // Then
                .andExpect(status().isNotFound());
    }

    private SupportTicketDto newTicketDto() {
        return new SupportTicketDto("Test Ticket " + new Random().nextInt(),
                "Test Description " + new Random().nextInt(),
                SupportTicket.Status.OPEN);
    }

    private SupportTicket insertData() throws Exception {
        SupportTicket newTicket = new SupportTicket(
                "Test Ticket " + new Random().nextInt(),
                "Test Description " + + new Random().nextInt(),
                SupportTicket.Status.OPEN);

        repository.save(newTicket);
        repository.flush();

        return newTicket;
    }

    private String toIsoTimestamp(Timestamp timestamp) {
       return isoFormatter.format(timestamp);
    }
}
