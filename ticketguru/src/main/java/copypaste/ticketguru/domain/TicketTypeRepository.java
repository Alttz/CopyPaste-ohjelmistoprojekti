package copypaste.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {

}
