package copypaste.ticketguru.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import copypaste.ticketguru.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketguruRestController {
	
	@Autowired
	private EventRepository erepository;
	@Autowired
	private TicketRepository trepository;
	@Autowired
	private PurchaseRepository prepository;

	// hae kaikki liput
	@GetMapping(value = "/api/tickets")
	public List<Ticket> getAllTickets() {
		return (List<Ticket>) trepository.findAll();
	}

	@PostMapping(value = "/api/purchasex")
	public ResponseEntity<Purchase> createPurchasex(@RequestBody Purchase newPurchase) {
		Purchase savedPurchase = prepository.save(newPurchase);
		//Loop all passed tickets through
		//Set their fk to savePurchase
		//for
			//luo jokaisen lipun
			//lipun fk on ostotapahtuman id



		return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
	}
	
	// Create a new meeting
    @PostMapping(value = "/api/purchases")
    public Purchase createPurchase(@RequestBody Purchase purchase,@RequestParam("id") long id) {
		Ticket temp_ticket = trepository.findById(id).get();
		//purchase.setTickets();
		List<Ticket> tickets = Arrays.asList(temp_ticket);
        return prepository.save(purchase);
    }

	@GetMapping(value = "/api/purchases")
	public List<Purchase> getAllPurchases() {
		return (List<Purchase>) prepository.findAll();
	}
	
	// hae kaikki tapahtumat
	@GetMapping(value = "/api/events")
	public List<Event> getAllEvents() {
		return (List<Event>) erepository.findAll();
	}
	
	// hae yksi tapahtuma ID:llä
	@GetMapping(value = "/api/events/{id}")
	public ResponseEntity<Event> getEventById(@PathVariable Long id) {
		Optional<Event> eventOpt = erepository.findById(id);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventOpt.get());
	}
	
	// hae tapahtumat kaupungin mukaan
	@GetMapping(value = "/api/events/byName")
	public ResponseEntity<List<Event>> findEventsByName(@RequestParam("name") String name) {
	    List<Event> events = erepository.findByNameContainingIgnoreCase(name);
	    if (events.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(events);
	}
	
	// hae tapahtumat tapahtuman nimen mukaan
	@GetMapping(value = "/api/events/byCity")
	public ResponseEntity<List<Event>> findEventsByCity(@RequestParam("city") String city) {
	    List<Event> events = erepository.findByCityIgnoreCase(city);
	    if (events.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(events);
	}

	@PutMapping(value = "/api/events/{id}")
	public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
		Optional<Event> event = erepository.findById(id);

		if(!event.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		updatedEvent.setId(event.get().getId());
		return ResponseEntity.ok(erepository.save(updatedEvent));
	}

	// Luo uusi tapahtuma
	@PostMapping(value = "/api/events")
	public ResponseEntity<Event> createEvent(@RequestBody Event newEvent) {

		Event savedEvent = erepository.save(newEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
	}

	//Delete
	@DeleteMapping("/api/delete/{id}")
	public ResponseEntity<Void> deleteEvent(long id) {
		if(!erepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Event item = erepository.findById(id).get();
		erepository.delete(item);
		return ResponseEntity.noContent().build();
	}

}
