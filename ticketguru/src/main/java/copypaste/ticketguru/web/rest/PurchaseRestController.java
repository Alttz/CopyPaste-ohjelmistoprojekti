package copypaste.ticketguru.web.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.Purchase;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketType;

import copypaste.ticketguru.domain.EventRepository;
import copypaste.ticketguru.domain.PurchaseRepository;
import copypaste.ticketguru.domain.PurchaseRequest;
import copypaste.ticketguru.domain.TicketRepository;
import copypaste.ticketguru.domain.TicketTypeRepository;
import copypaste.ticketguru.domain.UserRepository;


@RestController
public class PurchaseRestController {
    // 5 different repos in one controller make me very unhappy
    // But what can you do

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/api/purchases")
	public List<Purchase> getAllPurchases() {
		return (List<Purchase>) purchaseRepository.findAll();
	}

    // Luodaan ostotapahtuma. Toistaiseksi ei pysty käyttämään useampaan 
	@PostMapping("/api/purchases")
	public ResponseEntity<?> createPurchaseWithTickets(@RequestBody PurchaseRequest purchaseRequest) {
		// Tarkastetaan löytyykö tapahtuma id:n perusteella
        return eventRepository.findById(purchaseRequest.getEventId()).map(event -> {
            // tarkatetaan, että lippumäärä on riittävä
            if (event.getTicketCount() < purchaseRequest.getTicketTypeNames().size()) {
                return ResponseEntity.badRequest().body("Not enough tickets available for the event.");
            }

            // Tarkastetaan löytyykö käyttäjä id:n perusteella
            Optional<AppUser> userOpt = userRepository.findById(purchaseRequest.getUserId());
            if (!userOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            AppUser appUser = userOpt.get();

            // Haetaan lipputyypit tapahtuman ja nimen mukaan, jolloin pystytään toimittamaan vain lipputyyppien-nimet ostotapahtuman yhteydessä
            List<TicketType> ticketTypes = ticketTypeRepository.findByEventAndNameIn(event, purchaseRequest.getTicketTypeNames());
            
            // tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit tapahtuman lipputyypeistä
            if (ticketTypes.size() != purchaseRequest.getTicketTypeNames().size()) {
                return ResponseEntity.badRequest().body("One or more ticket types not found for the event.");
            }

            List<Ticket> tickets = ticketTypes.stream().map(tt -> new Ticket(tt, event, null, false)).collect(Collectors.toList());

            // Päivitä tapahtuman lippumäärä
            event.setTicketCount(event.getTicketCount() - tickets.size());
            eventRepository.save(event); 

            // tallenna ostotapahtuma
            Purchase purchase = new Purchase(new Date(), tickets, appUser);
            Purchase savedPurchase = purchaseRepository.save(purchase);

            // tallenna kullekin lipulle ostotapahtuma, johon ne kuuluu
            tickets.forEach(ticket -> {
                ticket.setPurchase(savedPurchase);
                ticketRepository.save(ticket);
            });

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
        }).orElse(ResponseEntity.notFound().build());	    
	}
}
