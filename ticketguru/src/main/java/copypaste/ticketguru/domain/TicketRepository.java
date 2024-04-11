package copypaste.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findByEvent(Event event);
    List<Ticket> findByTicketType(TicketType ticketType);
    Optional<Ticket> findByUuid(String uuid);
}
