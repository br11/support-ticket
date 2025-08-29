package io.github.br11.st.crud;

import io.github.br11.st.crud.dto.SupportTicketDto;
import io.github.br11.st.domain.SupportTicket;
import io.github.br11.st.integration.SupportTicketIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SupportTicketCrudService {

    private final SupportTicketRepository repository;
    private final SupportTicketIntegrationService integrationService;

    @Autowired
    public SupportTicketCrudService(SupportTicketRepository repository,
                                    SupportTicketIntegrationService integrationService) {
        this.repository = repository;
        this.integrationService = integrationService;
    }

    public SupportTicket create(SupportTicketDto dto) {
        SupportTicket createdTicket = repository.save(toEntity(dto));
        integrationService.create(createdTicket);
        return createdTicket;
    }

    @Transactional(readOnly = true)
    public Optional<SupportTicket> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SupportTicket> findAll() {
        return repository.findAll();
    }

    public Optional<SupportTicket> update(UUID id, SupportTicketDto dto) {
        Optional<SupportTicket> ticket = findById(id);
        if (ticket.isEmpty()) {
            return Optional.empty();
        }
        ticket.ifPresent(ticketFound -> {
            ticketFound.setTitle(dto.getTitle());
            ticketFound.setDescription(dto.getDescription());
            ticketFound.setStatus(dto.getStatus());
            repository.save(ticketFound);
            integrationService.update(ticketFound);
        });
        return ticket;
    }

    public void delete(UUID id) {
        findById(id).ifPresent(ticket -> {
            repository.deleteById(id);
            integrationService.delete(ticket);
        });
    }

    private SupportTicket toEntity(SupportTicketDto dto) {
        return new SupportTicket(
                dto.getTitle(),
                dto.getDescription(),
                dto.getStatus()
        );
    }


}
