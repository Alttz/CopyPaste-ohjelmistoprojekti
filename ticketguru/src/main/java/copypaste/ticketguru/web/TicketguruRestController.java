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

	// Lisää lipputyypit tapahtumaan
	@PostMapping("/api/events/{eventId}/tickettypes")
	public ResponseEntity<?> createTicketTypesForEvent(@PathVariable Long eventId, @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
		// Tarkastetaan löytyykö tapahtuma id:n perusteella
		Optional<Event> eventOpt = erepository.findById(eventId);
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    // Tarkista lipputyyppien nimet, luodaan ne ja määritetään mihin tapahtumaan ne kuuluu
	    List<TicketType> ticketTypes = new ArrayList<>();
	    for (TicketTypeRequest ttRequest : ticketTypeRequests) {
	        if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
	            return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
	        }
	        TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
	        ticketTypes.add(newTicketType);
	    }

	    List<TicketType> savedTicketTypes = (List<TicketType>) ttrepository.saveAll(ticketTypes);

	    // Yksinkertaistetaan JSON-vastausta
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
		// Tarkastetaan löytyykö tapahtuma id:n perusteella
	    Optional<Event> eventOpt = erepository.findById(purchaseRequest.getEventId());
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    // tarkatetaan, että lippumäärä on riittävä
	    if (event.getTicketCount() < purchaseRequest.getTicketTypeNames().size()) {
	        return ResponseEntity.badRequest().body("Not enough tickets available for the event.");
	    }

	    // Tarkastetaan löytyykö käyttäjä id:n perusteella
	    Optional<AppUser> userOpt = urepository.findById(purchaseRequest.getUserId());
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    AppUser appUser = userOpt.get();

	    // Haetaan lipputyypit tapahtuman ja nimen mukaan, jolloin pystytään toimittamaan vain lipputyyppien-nimet ostotapahtuman yhteydessä
	    List<TicketType> ticketTypes = ttrepository.findByEventAndNameIn(event, purchaseRequest.getTicketTypeNames());
	    
	    // tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit tapahtuman lipputyypeistä
	    if (ticketTypes.size() != purchaseRequest.getTicketTypeNames().size()) {
	        return ResponseEntity.badRequest().body("One or more ticket types not found for the event.");
	    }

	    List<Ticket> tickets = ticketTypes.stream().map(tt -> new Ticket(tt, event, null, false)).collect(Collectors.toList());

	    // Päivitä tapahtuman lippumäärä
	    event.setTicketCount(event.getTicketCount() - tickets.size());
	    erepository.save(event); 

	    // tallenna ostotapahtuma
	    Purchase purchase = new Purchase(new Date(), tickets, appUser);
	    Purchase savedPurchase = prepository.save(purchase);

	    // tallenna kullekin lipulle ostotapahtuma, johon ne kuuluu
	    tickets.forEach(ticket -> {
	        ticket.setPurchase(savedPurchase);
	        trepository.save(ticket);
	    });

	    return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
	}
	
	// Päivität tapahtuman lipputyyppejä
	@PutMapping("/api/events/{eventId}/tickettypes")
	public ResponseEntity<?> updateTicketTypesForEvent(@PathVariable Long eventId, @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
		// Tarkastetaan löytyykö tapahtuma id:n perusteella
	    Optional<Event> eventOpt = erepository.findById(eventId);
	    if (!eventOpt.isPresent()) {
	        return ResponseEntity.notFound().build();
	    }
	    Event event = eventOpt.get();

	    // Poistetaan aikaisemmat lipputyypit
	    List<TicketType> existingTicketTypes = ttrepository.findByEvent(event);
	    ttrepository.deleteAll(existingTicketTypes);

	    // Tarkastetaan lipputyyppien nimet, luodaan uudet lipputyypit ja määritetään mihin tapahtumaan ne kuuluvat
	    List<TicketType> ticketTypes = new ArrayList<>();
	    for (TicketTypeRequest ttRequest : ticketTypeRequests) {
	        if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
	            return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
	        }
	        TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
	        ticketTypes.add(newTicketType);
	    }

	    List<TicketType> savedTicketTypes = (List<TicketType>) ttrepository.saveAll(ticketTypes);

	    // Yksinkertaistetaan JSON-vastausta
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
}
