package copypaste.ticketguru.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
	
    private static final Set<String> ALLOWED_TICKET_TYPES = Set.of("Aikuinen", "Lapsi", "Eläkeläinen", "Opiskelija", "Varusmies", "VIP");

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
	public ResponseEntity<Event> createEvent(@RequestBody Event newEvent) {
		Event savedEvent = erepository.save(newEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
	}

	// Lisää lipputyypit tapahtumaan
	@PostMapping("/api/events/{eventId}/tickettypes")
	public ResponseEntity<?> createTicketTypesForEvent(@PathVariable Long eventId, @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
	    Optional<Event> eventOpt = erepository.findById(eventId);
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    // Tarkista lipputyyppien nimet ja luo
	    List<TicketType> ticketTypes = new ArrayList<>();
	    for (TicketTypeRequest ttRequest : ticketTypeRequests) {
	        if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
	            return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
	        }
	        TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
	        ticketTypes.add(newTicketType);
	    }

	    List<TicketType> savedTicketTypes = (List<TicketType>) ttrepository.saveAll(ticketTypes);

	    // Yksinkertaistettu vastaus
	    List<Map<String, Object>> response = savedTicketTypes.stream().map(tt -> {
	        Map<String, Object> ticketTypeResponse = new HashMap<>();
	        ticketTypeResponse.put("id", tt.getId());
	        ticketTypeResponse.put("name", tt.getName());
	        ticketTypeResponse.put("price", tt.getPrice());
	        ticketTypeResponse.put("event", tt.getEvent().getId()); // sisällytä vain ID
	        return ticketTypeResponse;
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Luodaan ostotapahtuma. Toistaiseksi ei pysty käyttämään useampaan 
	@PostMapping("/api/purchases")
	public ResponseEntity<?> createPurchaseWithTickets(@RequestBody PurchaseRequest purchaseRequest) {
	    Optional<Event> eventOpt = erepository.findById(purchaseRequest.getEventId());
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    if (event.getTicketCount() < purchaseRequest.getTicketTypeNames().size()) {
	        return ResponseEntity.badRequest().body("Not enough tickets available for the event.");
	    }

	    Optional<AppUser> userOpt = urepository.findById(purchaseRequest.getUserId());
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    AppUser appUser = userOpt.get();

	    // Haetaan lipputyypit tapahtuman ja nimen mukaan, jolloin pystytään toimittamaan vain lipputyyppien-nimet ostotapahtuman yhteydessä
	    List<TicketType> ticketTypes = ttrepository.findByEventAndNameIn(event, purchaseRequest.getTicketTypeNames());
	    
	    if (ticketTypes.size() != purchaseRequest.getTicketTypeNames().size()) {
	        return ResponseEntity.badRequest().body("One or more ticket types not found for the event.");
	    }

	    List<Ticket> tickets = ticketTypes.stream().map(tt -> new Ticket(tt, event, null, false)).collect(Collectors.toList());

	    // Päivitä tapahtuman lippumäärä
	    event.setTicketCount(event.getTicketCount() - tickets.size());
	    erepository.save(event); 

	    Purchase purchase = new Purchase(new Date(), tickets, appUser);
	    Purchase savedPurchase = prepository.save(purchase);

	    tickets.forEach(ticket -> {
	        ticket.setPurchase(savedPurchase);
	        trepository.save(ticket);
	    });

	    return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
	}
	
	// Päivität tapahtuman lipputyyppejä
	@PutMapping("/api/events/{eventId}/tickettypes")
	public ResponseEntity<?> updateTicketTypesForEvent(@PathVariable Long eventId, @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
	    Optional<Event> eventOpt = erepository.findById(eventId);
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    // Poistetaan aikaisemmat lipputyypit
	    List<TicketType> existingTicketTypes = ttrepository.findByEvent(event);
	    ttrepository.deleteAll(existingTicketTypes);

	    // Tarkastetaan lipputyyppien nimet ja luodaan uudet
	    List<TicketType> ticketTypes = new ArrayList<>();
	    for (TicketTypeRequest ttRequest : ticketTypeRequests) {
	        if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
	            return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
	        }
	        TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
	        ticketTypes.add(newTicketType);
	    }

	    List<TicketType> savedTicketTypes = (List<TicketType>) ttrepository.saveAll(ticketTypes);

	    // Yksinkertaistettu vastasus
	    List<Map<String, Object>> response = savedTicketTypes.stream().map(tt -> {
	        Map<String, Object> ticketTypeResponse = new HashMap<>();
	        ticketTypeResponse.put("id", tt.getId());
	        ticketTypeResponse.put("name", tt.getName());
	        ticketTypeResponse.put("price", tt.getPrice());
	        ticketTypeResponse.put("event", tt.getEvent().getId()); // Sisällytetään vastaukseen vain tapahtuman id
	        return ticketTypeResponse;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}


	// Päivitä tapahtumaa
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

	// Poista tapahtuma
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
