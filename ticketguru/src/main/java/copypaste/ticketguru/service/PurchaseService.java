package copypaste.ticketguru.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import copypaste.ticketguru.domain.*;

@Service
public class PurchaseService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TicketTypeRepository ticketTypeRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private TicketRepository ticketRepository;

    // Yksittäisen ostotapahtuman käsittelyn logiikka
	@Transactional
    public Optional<Purchase> processPurchaseRequest(PurchaseRequest purchaseRequest) {
		// Tarkastetaan löytyykö tapahtuma id:n perusteella
		return eventRepository.findById(purchaseRequest.getEventId()).flatMap(event -> {

			// Laske, kuinka monta kutakin lipputyyppiä on pyynnössä
			Map<String, Long> ticketTypeCounts = purchaseRequest.getTicketTypeNames().stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			// Laske lippujen totaalimäärä
			long totalTicketsRequested = ticketTypeCounts.values().stream().mapToLong(Long::longValue).sum();

			// Tarkasta onko tapahtumaan riittävästi lippuja
			if (event.getTicketCount() < totalTicketsRequested) {
				return Optional.empty();
			}

			// Tarkastetaan löytyykö käyttäjä id:n perusteella
			Optional<AppUser> userOpt = userRepository.findById(purchaseRequest.getUserId());
			if (!userOpt.isPresent()) {
				return Optional.empty();
			}

			// Hae lipputyypit tapahtuman ja lipputyypin nimen perusteella
			List<TicketType> ticketTypes = ticketTypeRepository.findByEventAndNameIn(event,
					new ArrayList<>(ticketTypeCounts.keySet()));

			// Tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit tapahtuman lipputyypeistä
			if (ticketTypes.size() != ticketTypeCounts.keySet().size()) {
				return Optional.empty();
			}

			// Luo liput ja määritä niille pyynnössä annetut lipputyypit
			List<Ticket> tickets = new ArrayList<>();
		    for (TicketType tt : ticketTypes) {
		        long count = ticketTypeCounts.getOrDefault(tt.getName(), 0L);
		        for (int i = 0; i < count; i++) {
		            tickets.add(new Ticket(tt, event, null, false));
		        }
		    }

            // Luodaan ostotapahtuma, jossa lista lipuista on aluksi tyhjä
		    Purchase purchase = new Purchase(new Date(), new ArrayList<>(), userOpt.get());
		    
            // Tallenna ostotapahtuma, jotta siihen generoituu ID
		    Purchase savedPurchase = purchaseRepository.save(purchase);
            
            // Määritetään mihin ostotapahtumaan liput kuuluu ja lisätään ne ostotapahtumalle
		    tickets.forEach(ticket -> {
		        ticket.setPurchase(savedPurchase); // Set the purchase
		        Ticket savedTicket = ticketRepository.save(ticket); // Save the ticket
		        savedPurchase.getTickets().add(savedTicket); // Optionally re-associate saved tickets with the purchase
		    });

            // Päivitä tapahtuman lippumäärä ja tallenna
            event.setTicketCount(event.getTicketCount() - (int) totalTicketsRequested);
            eventRepository.save(event);
            
            purchaseRepository.save(savedPurchase);

            // Palauta tallennettu ostotapahtuma
            return Optional.of(savedPurchase);
        });
	}

}
