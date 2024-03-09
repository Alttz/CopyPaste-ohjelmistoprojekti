package copypaste.ticketguru.web.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;

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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> createPurchaseWithTickets(@Valid @RequestBody List<PurchaseRequest> purchaseRequestList) {
        // This is a very STUPID IDEA AND SHOULD NOT BE MERGED FOR THE LOVE OF GOD
        // Please forgive me for this monstrosity that breaks every single convention of API design
        class PurchaseRequestResponse {
            private boolean isSuccessful;
            private String errorMessage;
    
            public boolean isSuccessful() {
                return isSuccessful;
            }
            public void setSuccessful(boolean isSuccessful) {
                this.isSuccessful = isSuccessful;
            }
            public String getErrorMessage() {
                return errorMessage;
            }
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
            public PurchaseRequestResponse(boolean isSuccessful, String errorMessage) {
                this.isSuccessful = isSuccessful;
                this.errorMessage = errorMessage;
            }
            public PurchaseRequestResponse() {
                this.isSuccessful = false;
                this.errorMessage = "";
            }
        }

        HashMap<Long, PurchaseRequestResponse> responseMap = new HashMap<>();

        boolean atLeastOneWasSuccessful = false;

        for(PurchaseRequest purchaseRequest : purchaseRequestList) {
            PurchaseRequestResponse resp = eventRepository.findById(purchaseRequest.getEventId()).map(event -> {
                PurchaseRequestResponse status = new PurchaseRequestResponse();

                if(event.getTicketCount() < purchaseRequest.getTicketTypeNames().size()) {
                    status.setErrorMessage("Tapahtumaan ei ole riittävästi lippuja vapaana.");
                    return status;
                }

                Optional<AppUser> userOpt = userRepository.findById(purchaseRequest.getUserId());
                if (!userOpt.isPresent()) {
                    status.setErrorMessage("Käyttäjää ei löytynyt");
                    return status;
                }
                AppUser appUser = userOpt.get();

                List<TicketType> ticketTypes = ticketTypeRepository.findByEventAndNameIn(event,
                purchaseRequest.getTicketTypeNames());

                // tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit
                // tapahtuman lipputyypeistä
                if (ticketTypes.size() != purchaseRequest.getTicketTypeNames().size()) {
                    status.setErrorMessage("Yhtä tai useampaa määritettyä lipputyyppiä ei löydy tälle tapahtumalle.");
                    return status;
                }

                List<Ticket> tickets = ticketTypes.stream().map(tt -> new Ticket(tt, event, null, false))
                        .collect(Collectors.toList());

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

                status.setSuccessful(true);

                return status;
            }).orElse(new PurchaseRequestResponse(false, "Tapahtumaa ei löytynyt"));

            if(!atLeastOneWasSuccessful) {
                atLeastOneWasSuccessful = resp.isSuccessful();
            }

            responseMap.put(purchaseRequest.getEventId(), resp);
        }

        if(!atLeastOneWasSuccessful) {
            return ResponseEntity.badRequest().body(responseMap);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }
}
