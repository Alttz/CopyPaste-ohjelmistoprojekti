package copypaste.ticketguru.web.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;
import copypaste.ticketguru.securingweb.JwtUtil;

@RestController
public class TicketRestController {
	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	private JwtUtil jwtUtil;

	private Boolean RestValidateJWT(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7); // Extract the token from the header
			// Validate the token
			return jwtUtil.validateToken(token);
		}
		return false;
	}

	// hae kaikki liput
	@GetMapping(value = "/api/tickets")
	public ResponseEntity<?> getAllTickets(
			@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if (RestValidateJWT(authHeader)) {
			List<Ticket> tickets = (List<Ticket>) ticketRepository.findAll();
			return ResponseEntity.ok(tickets); 
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
	}
}
