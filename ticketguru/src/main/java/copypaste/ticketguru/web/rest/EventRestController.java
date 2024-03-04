package copypaste.ticketguru.web.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;
import copypaste.ticketguru.domain.EventRequest;

@RestController
public class EventRestController {
    @Autowired
    EventRepository repository;

    // hae kaikki tapahtumat
	@GetMapping(value = "/api/events")
	public List<Event> getAllEvents() {
		return (List<Event>) repository.findAll();
	}

	// hae yksi tapahtuma ID:llä
	@GetMapping(value = "/api/events/{id}")
	public ResponseEntity<Event> getEventById(@PathVariable Long id) {
		Optional<Event> eventOpt = repository.findById(id);
		if (!eventOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(eventOpt.get());
	}

	// hae tapahtumat kaupungin mukaan
	@GetMapping(value = "/api/events/byName")
	public ResponseEntity<List<Event>> findEventsByName(@RequestParam("name") String name) {
		List<Event> events = repository.findByNameContainingIgnoreCase(name);
		if (events.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(events);
	}

	// hae tapahtumat tapahtuman nimen mukaan
	@GetMapping(value = "/api/events/byCity")
	public ResponseEntity<List<Event>> findEventsByCity(@RequestParam("city") String city) {
		List<Event> events = repository.findByCityIgnoreCase(city);
		if (events.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(events);
	}

    // Lisää tapahtuma
	@PostMapping(value = "/api/events")
	public ResponseEntity<Event> createEvent(@RequestBody Event newEvent) {
		Event savedEvent = repository.save(newEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
	}

    @PutMapping(value = "/api/events/{id}")
	public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
		return repository.findById(id).map(event -> {
			event.setName(eventRequest.getName());
			event.setDate(eventRequest.getDate());
			event.setPlace(eventRequest.getPlace());
			event.setCity(eventRequest.getCity());
			event.setTicketCount(eventRequest.getTicketCount());

			Event updatedEvent = repository.save(event);
			return ResponseEntity.ok(updatedEvent);
		}).orElse(ResponseEntity.notFound().build());
	}

    @DeleteMapping("/api/delete/{id}")
	public ResponseEntity<?> deleteEvent(@PathVariable long id) {
        return repository.findById(id).map(event -> {
            repository.delete(event);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
	}
}
