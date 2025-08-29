package io.github.br11.st.crud;

import io.github.br11.st.crud.dto.SupportTicketDto;
import io.github.br11.st.domain.SupportTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/support-tickets")
public class SupportTicketController {
    
    private static final Logger log = LoggerFactory.getLogger(SupportTicketController.class);

    private final SupportTicketCrudService supportTicketCrudService;

    @Autowired
    public SupportTicketController(SupportTicketCrudService supportTicketCrudService) {
        this.supportTicketCrudService = supportTicketCrudService;
    }

    @PostMapping
    public ResponseEntity<SupportTicket> create(@RequestBody SupportTicketDto ticketDto) {
        log.info("Creating support ticket: {}", ticketDto);
        SupportTicket ticket = supportTicketCrudService.create(ticketDto);
        log.info("Support ticket created: {}", ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getById(@PathVariable UUID id) {
        log.info("Finding support ticket: {}", id);
        Optional<SupportTicket> ticket = supportTicketCrudService.findById(id);
        log.info("Support ticket found: {}", ticket);
        return ResponseEntity.of(ticket);
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAll() {
        log.info("Finding all support tickets: ");
        List<SupportTicket> tickets = supportTicketCrudService.findAll();
        log.info("All support tickets found: {}", tickets);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicket> update(@PathVariable UUID id, @RequestBody SupportTicketDto ticketDto) {
        log.info("Updating support ticket: {} {}", id, ticketDto);
        Optional<SupportTicket> ticket = supportTicketCrudService.update(id, ticketDto);
        if (ticket.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Support ticket updated: {}", ticket);
        return ResponseEntity.status(HttpStatus.OK).body(ticket.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SupportTicket> delete(@PathVariable UUID id) {
        log.info("Deleting support ticket: {}", id);
        supportTicketCrudService.delete(id);
        log.info("Support ticket deleted: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
