package copypaste.ticketguru.web.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.RESTError;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;
import copypaste.ticketguru.service.JwtValidatorService;

@RestController
public class TicketRestController {
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	private JwtValidatorService jwtValidatorService;

	// hae kaikki liput
	@GetMapping(value = "/api/tickets")
	public ResponseEntity<?> getAllTickets(
			@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if (jwtValidatorService.validateToken(authHeader)) {
			List<Ticket> tickets = (List<Ticket>) ticketRepository.findAll();
			return ResponseEntity.ok(tickets); 
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RESTError("Invalid or missing token"));
	}
	
	@GetMapping(value = "/api/tickets/{id}")
    public ResponseEntity<?> getTicketById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (jwtValidatorService.validateToken(authHeader)) {
            Optional<Ticket> ticket = ticketRepository.findById(id);
            if (ticket.isPresent()) {
                return ResponseEntity.ok(ticket.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RESTError("Invalid or missing token"));
    }
}
