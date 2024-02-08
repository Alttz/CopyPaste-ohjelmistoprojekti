package copypaste.ticketguru.domain.Ticket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import copypaste.ticketguru.domain.Event.Event;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findByEvent(Event event);
}
