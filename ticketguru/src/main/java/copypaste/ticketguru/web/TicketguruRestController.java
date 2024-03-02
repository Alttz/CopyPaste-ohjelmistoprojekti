package copypaste.ticketguru.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Date;
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

	// Luo uusi tapahtuma
	@PostMapping(value = "/api/events")
	public ResponseEntity<Event> createEvent(@RequestBody Event newEvent) {

		Event savedEvent = erepository.save(newEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
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

	// Luo ostotapahtuma, jossa toimitetaan tieto myytävistä lipusta ID:inä
	/*
	 * @PostMapping(value = "/api/purchases") public ResponseEntity<Purchase>
	 * createPurchase(@RequestBody PurchaseRequestDTO purchaseRequest) {
	 * List<Ticket> tickets = ((Collection<Ticket>)
	 * trepository.findAllById(purchaseRequest.getTickets())).stream()
	 * .collect(Collectors.toList());
	 * 
	 * if (tickets.size() != purchaseRequest.getTickets().size()) { return
	 * ResponseEntity.badRequest().build(); }
	 * 
	 * Purchase purchase = new Purchase(); purchase.setPurchaseDate(new Date()); //
	 * Määritetään ostoajankohta automaattisesti purchase.setTickets(tickets);
	 * tickets.forEach(ticket -> ticket.setPurchase(purchase)); // Linkataan lippu
	 * ostotapahtumaan
	 * 
	 * Purchase savedPurchase = prepository.save(purchase);
	 * 
	 * return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase); }
	 */

	// Luo ostotapahtuma ja liput samassa kutsussa
	@PostMapping("/api/purchaseswithtickets")
	public ResponseEntity<?> createPurchaseWithTickets(@RequestBody PurchaseRequest purchaseRequest) {
		// Etsitään tapahtuma ID:n perusteella
		Optional<Event> eventOpt = erepository.findById(purchaseRequest.getEventId());
		if (!eventOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Event event = eventOpt.get();

		// Tarkastetaan onko riittävästi lippuja myytäväksi
		if (event.getTicketCount() < purchaseRequest.getTickets().size()) {
			return ResponseEntity.badRequest().body("Not enough tickets left for the event.");
		}

		// Fetch the AppUser (assuming the AppUser ID is part of the PurchaseRequest)
		Optional<AppUser> userOpt = urepository.findById(purchaseRequest.getUserId());
		if (!userOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		AppUser appUser = userOpt.get();

		// Luodaan uusi ostotapahtuma
		Purchase purchase = new Purchase();
		purchase.setPurchaseDate(new Date()); // Määritetään ostotapahtuman päivämäärä automaattisesti
	    purchase.setAppUser(appUser); // Associate the purchase with the AppUser
		
		// Luodaan liput ostoa varten
		List<Ticket> tickets = purchaseRequest.getTickets().stream().map(ticketReq -> {
			Ticket ticket = new Ticket();
			ticket.setType(ticketReq.getType());
			ticket.setPrice(ticketReq.getPrice());
			ticket.setEvent(event);
			ticket.setPurchase(purchase); // Linkataan liput kyseiseen ostotapahtumaan
			ticket.setUsed(false);
			return ticket;
		}).collect(Collectors.toList());

		purchase.setTickets(tickets);

		// Vähennetään tapahtuman lippumäärä
		event.setTicketCount(event.getTicketCount() - tickets.size());

		// Tallennetaan tapahtuma ja ostotapahtuma lippuineen
		erepository.save(event); // Tallennetaan päivitetty lippumäärä
		Purchase savedPurchase = prepository.save(purchase);
		trepository.saveAll(tickets);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
	}

	// Päivitä tapahtumaa
	@PutMapping(value = "/api/events/{id}")
	public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
		Optional<Event> event = erepository.findById(id);

		if (!event.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		updatedEvent.setId(event.get().getId());
		return ResponseEntity.ok(erepository.save(updatedEvent));
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
