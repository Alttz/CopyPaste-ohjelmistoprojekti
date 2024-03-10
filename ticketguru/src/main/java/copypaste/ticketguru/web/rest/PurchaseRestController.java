package copypaste.ticketguru.web.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
            // Laske, kuinka monta kutakin lipputyyppiä on pyynnössä
        	Map<String, Long> ticketTypeCounts = purchaseRequest.getTicketTypeNames().stream()
        		    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // Laske lippujen totaalimäärä
            long totalTicketsRequested = ticketTypeCounts.values().stream().mapToLong(Long::longValue).sum();
            
            // Tarkasta onko tapahtumaan riittävästi lippuja
            if (event.getTicketCount() < totalTicketsRequested) {
                return ResponseEntity.badRequest().body("Lippuja ei ole riittävästi jäljellä tapahtumaan.");
            }

            // Tarkastetaan löytyykö käyttäjä id:n perusteella
            Optional<AppUser> userOpt = userRepository.findById(purchaseRequest.getUserId());
            if (!userOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            AppUser appUser = userOpt.get();

            // Hae lipputyypit tapahtuman ja lipputyypin nimen perusteella
            List<TicketType> ticketTypes = ticketTypeRepository.findByEventAndNameIn(event,
                    new ArrayList<>(ticketTypeCounts.keySet()));

            // Tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit tapahtuman lipputyypeistä            
            if (ticketTypes.size() != ticketTypeCounts.keySet().size()) {
                return ResponseEntity.badRequest().body("Yhtä tai useampaa määritettyä lipputyyppiä ei löydy tapahtumaan.");
            }

            // Luo liput ja määritä niille pyynnössä annetut lipputyypit
            List<Ticket> tickets = new ArrayList<>();
            for (TicketType tt : ticketTypes) {
                long count = ticketTypeCounts.getOrDefault(tt.getName(), 0L);
                for (int i = 0; i < count; i++) {
                    tickets.add(new Ticket(tt, event, null, false));
                }
            }

            // Päivitä tapahtuman lippumäärä
            event.setTicketCount((int) (event.getTicketCount() - totalTicketsRequested));
            eventRepository.save(event);

            // Tallenna ostotaphatuma
            Purchase purchase = new Purchase(new Date(), tickets, appUser);
            Purchase savedPurchase = purchaseRepository.save(purchase);

            // Tallenna kullekin lipulle ostotapahtuma, johon ne kuuluu
            tickets.forEach(ticket -> {
                ticket.setPurchase(savedPurchase);
                ticketRepository.save(ticket);
            });

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
        }).orElse(ResponseEntity.notFound().build());
    }
}
