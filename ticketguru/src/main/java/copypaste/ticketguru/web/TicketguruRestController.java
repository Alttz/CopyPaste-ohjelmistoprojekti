package copypaste.ticketguru.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

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
	@Autowired
	private UserRepository urepository;
	@Autowired
	private TicketTypeRepository ttrepository;

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

	// hae kaikki liput
	@GetMapping(value = "/api/tickets")
	public List<Ticket> getAllTickets() {
		return (List<Ticket>) trepository.findAll();
	}

	// hae kaikki ostot
	@GetMapping(value = "/api/purchases")
	public List<Purchase> getAllPurchases() {
		return (List<Purchase>) prepository.findAll();
	}

	// Lisää tapahtuma
	@PostMapping(value = "/api/events")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDate(eventRequest.getDate());
        event.setPlace(eventRequest.getPlace());
        event.setCity(eventRequest.getCity());
        event.setTicketCount(eventRequest.getTicketCount());

        Event savedEvent = erepository.save(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }
	
	// Lisää lipputyypit tapahtumaan
	@PostMapping("/api/ticketTypes/{eventId}")
	public ResponseEntity<?> createTicketTypes(@PathVariable Long eventId, @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
	    Optional<Event> eventOpt = erepository.findById(eventId);
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    List<Map<String, Object>> simplifiedResponse = ticketTypeRequests.stream().map(ttRequest -> {
	        TicketType ticketType = new TicketType();
	        ticketType.setName(ttRequest.getName());
	        ticketType.setPrice(ttRequest.getPrice());
	        ticketType.setEvent(event);
	        ttrepository.save(ticketType);
	        
	        // Valmistele yksinkertaistettu vastaus metodin vastauksena
	        Map<String, Object> ticketTypeResponse = new HashMap<>();
	        ticketTypeResponse.put("id", ticketType.getId());
	        ticketTypeResponse.put("name", ticketType.getName());
	        ticketTypeResponse.put("price", ticketType.getPrice());
	        ticketTypeResponse.put("event", ticketType.getEvent().getId());
	        
	        return ticketTypeResponse;
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.CREATED).body(simplifiedResponse);
	}


	// Luo uusi lippu
	@PostMapping("/api/tickets/{eventId}")
	public ResponseEntity<Ticket> createTicket(@PathVariable Long eventId, @RequestBody Ticket ticket) {
		return erepository.findById(eventId).map(event -> {
			ticket.setEvent(event);
			ticket.setPurchase(null);
			ticket.setUsed(false);
			Ticket savedTicket = trepository.save(ticket);
			return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
		}).orElse(ResponseEntity.notFound().build());
	}

	// Luo ostotapahtuma ja liput samassa kutsussa
	@PostMapping("/api/purchases")
	public ResponseEntity<?> createPurchaseWithTickets(@RequestBody PurchaseRequest purchaseRequest) {
		Optional<Event> eventOpt = erepository.findById(purchaseRequest.getEventId());
		if (!eventOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Event event = eventOpt.get();

		// Tarkista tapahtuman lippumäärä
		if (event.getTicketCount() < purchaseRequest.getTicketTypeIds().size()) {
			return ResponseEntity.badRequest().body("Not enough tickets available for the event.");
		}

		Optional<AppUser> userOpt = urepository.findById(purchaseRequest.getUserId());
		if (!userOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		AppUser appUser = userOpt.get();

		List<TicketType> ticketTypes = (List<TicketType>) ttrepository.findAllById(purchaseRequest.getTicketTypeIds());

		if (ticketTypes.size() != purchaseRequest.getTicketTypeIds().size()) {
			return ResponseEntity.badRequest().body("One or more ticket types not found.");
		}

		Purchase purchase = new Purchase(new Date(), new ArrayList<>(), appUser);

		for (TicketType ticketType : ticketTypes) {
			Ticket ticket = new Ticket(ticketType, event, purchase, false);
			purchase.getTickets().add(ticket);
		}

		 // Päivitä lippumäärä
	    event.setTicketCount(event.getTicketCount() - purchaseRequest.getTicketTypeIds().size());
	    erepository.save(event);
	    
		Purchase savedPurchase = prepository.save(purchase);
		trepository.saveAll(purchase.getTickets());

		// Luo yksinkertaistettu vastaus metodiin
	    List<Map<String, Object>> simplifiedTicketsResponse = savedPurchase.getTickets().stream().map(ticket -> {
	        Map<String, Object> ticketResponse = new HashMap<>();
	        ticketResponse.put("id", ticket.getId());
	        ticketResponse.put("ticketType", ticket.getTicketType().getId());
	        ticketResponse.put("event", ticket.getEvent().getId());
	        ticketResponse.put("used", ticket.isUsed());
	        return ticketResponse;
	    }).collect(Collectors.toList());

	    Map<String, Object> simplifiedPurchaseResponse = new HashMap<>();
	    simplifiedPurchaseResponse.put("id", savedPurchase.getId());
	    simplifiedPurchaseResponse.put("purchaseDate", savedPurchase.getPurchaseDate());
	    simplifiedPurchaseResponse.put("tickets", simplifiedTicketsResponse);
	    simplifiedPurchaseResponse.put("appUser", savedPurchase.getAppUser().getUser_id());

	    return ResponseEntity.status(HttpStatus.CREATED).body(simplifiedPurchaseResponse);
	}
	@PutMapping(value = "/api/events/{id}")
	public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
	    return erepository.findById(id).map(event -> {
	        event.setName(eventRequest.getName());
	        event.setDate(eventRequest.getDate());
	        event.setPlace(eventRequest.getPlace());
	        event.setCity(eventRequest.getCity());
	        event.setTicketCount(eventRequest.getTicketCount());

	        Event updatedEvent = erepository.save(event);
	        return ResponseEntity.ok(updatedEvent);
	    }).orElse(ResponseEntity.notFound().build());
	}


	// Delete
	@DeleteMapping("/api/delete/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
		if (!erepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Event item = erepository.findById(id).get();
		erepository.delete(item);
		return ResponseEntity.noContent().build();
	}

}
