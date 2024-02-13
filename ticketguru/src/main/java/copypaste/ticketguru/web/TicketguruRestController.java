package copypaste.ticketguru.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;

@RestController
public class TicketguruRestController {
	
	@Autowired
	private EventRepository erepository;
	
	@GetMapping(value = "/events")
	public @ResponseBody List<Event> eventListRest() {
		return (List<Event>) erepository.findAll();
	}


}
