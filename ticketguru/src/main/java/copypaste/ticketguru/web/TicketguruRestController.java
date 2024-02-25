package copypaste.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;

@RestController
public class TicketguruRestController {
	
	@Autowired
	private EventRepository erepository;
	
	// hae kaikki tapahtumat
	@GetMapping(value = "/api/events")
	public List<Event> getAllEvents() {
		return (List<Event>) erepository.findAll();
	}
	
	// hae yksi tapahtuma ID:llä
	@GetMapping(value = "/api/event/{id}")
	public ResponseEntity<Event> getEventById(@PathVariable Long id) {
		Optional<Event> eventOpt = erepository.findById(id);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventOpt.get());
	}
	
	// hae tapahtumat kaupungin mukaan
	@GetMapping(value = "/api/events/search/byName")
	public ResponseEntity<List<Event>> findEventsByName(@RequestParam("name") String name) {
	    List<Event> events = erepository.findByNameContainingIgnoreCase(name);
	    if (events.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(events);
	}
	
	// hae tapahtumat tapahtuman nimen mukaan
	@GetMapping(value = "/api/events/search/byCity")
	public ResponseEntity<List<Event>> findEventsByCity(@RequestParam("city") String city) {
	    List<Event> events = erepository.findByCityIgnoreCase(city);
	    if (events.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(events);
	}

	// Since we are already passing the event in, no need to care too much about passing a separate eventid I guess?
	@PutMapping(value = "api/events/update")
	public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
		if(!erepository.existsById(event.getId())) {
			return ResponseEntity.notFound().build();
		}

		erepository.save(event);

		return ResponseEntity.ok(event);
	}


}
