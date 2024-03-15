package copypaste.ticketguru.web.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;

@RestController
public class TicketRestController {
	@Autowired
	TicketRepository ticketRepository;

	// hae kaikki liput
	@GetMapping(value = "/api/tickets")
	public List<Ticket> getAllTickets() {
		return (List<Ticket>) ticketRepository.findAll();
	}
}
