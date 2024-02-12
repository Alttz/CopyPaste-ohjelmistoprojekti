package copypaste.ticketguru.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import copypaste.ticketguru.domain.EventRepository;

@Controller
public class TicketController {
	
	@Autowired
	private EventRepository repository;
	
	@RequestMapping("/eventlist")
	public String showEventlist(Model model) {
		model.addAttribute("events", repository.findAll());
		return "eventlist";
	}
	
	@RequestMapping("/hello")
	public String showMainPage() {
		return "hello";
	}


}
