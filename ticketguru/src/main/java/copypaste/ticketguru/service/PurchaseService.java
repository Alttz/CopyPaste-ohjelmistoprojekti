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

			// Tarkastetaan lipputyypin nimen perusteella löytyykö määritetyt lipputyypit
			// tapahtuman lipputyypeistä
			if (ticketTypes.size() != ticketTypeCounts.keySet().size()) {
				return Optional.empty();
			}

			// Luo liput ja määritä niille pyynnössä annetut lipputyypit
			List<Ticket> tickets = new ArrayList<>();
			ticketTypes.forEach(tt -> {
				long count = ticketTypeCounts.getOrDefault(tt.getName(), 0L);
				tickets.addAll(createTicketsForType(count, tt, event));
			});

			// Päivitä tapahtuman lippumäärä
			event.setTicketCount(event.getTicketCount() - (int) totalTicketsRequested);
			eventRepository.save(event);

			// Tallenna ostotapahtuma
			Purchase purchase = new Purchase(new Date(), tickets, userOpt.get());
			return Optional.of(purchaseRepository.save(purchase));
		});
	}

    // Metodi, jolla luodan tietty määrä lipputyypin lippuja.
    private List<Ticket> createTicketsForType(long count, TicketType ticketType, copypaste.ticketguru.domain.Event event) {
		List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(ticketType, event, null, false));
        }
        return tickets;
	}
}