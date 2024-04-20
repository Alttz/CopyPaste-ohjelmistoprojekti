package copypaste.ticketguru.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
	
	List<Event> findByNameContainingIgnoreCase(String name);
	List<Event> findByCityIgnoreCase(String city);
	Event findEventById(Long id);





}
