package copypaste.ticketguru.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {
	
    List<TicketType> findByEventAndNameIn(Event event, List<String> names);
    List<TicketType> findByEvent(Event event);

}
