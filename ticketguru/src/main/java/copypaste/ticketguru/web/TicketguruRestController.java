package copypaste.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

	//Delete
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
		if(!erepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Event item = erepository.findById(id).get();
		erepository.delete(item);
		//erepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

	/*
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteEvent(@PathVariable long id) {
		if(!erepository.existsById(id)) {
			return new ResponseEntity<>("HELLO",HttpStatus.NO_CONTENT);
		}
		//Tried using responseEntity differently to get an output.
		if(!erepository.existsById(id)) {
			return new ResponseEntity<>("HELLO",HttpStatus.NO_CONTENT);
		}
		erepository.delete(erepository.findById(id).get());
		return new ResponseEntity<>("HELLO", HttpStatus.OK);
	}
	*/



}
