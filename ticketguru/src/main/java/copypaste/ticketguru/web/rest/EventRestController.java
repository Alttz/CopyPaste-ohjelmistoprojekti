package copypaste.ticketguru.web.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import copypaste.ticketguru.domain.*;
import jakarta.validation.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventRestController {
    @Autowired
    EventRepository eventRepository;
    // Looks evil, should probably have its own file, but don't know how to make it
    // nice in that case
    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Autowired
    TicketRepository ticketRepository;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private static final Set<String> ALLOWED_TICKET_TYPES = Set.of("Aikuinen", "Lapsi", "Eläkeläinen", "Opiskelija",
            "Varusmies", "VIP");

    // hae kaikki tapahtumat
    @GetMapping(value = "/api/events")
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    // hae yksi tapahtuma ID:llä
    @GetMapping(value = "/api/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventOpt.get());
    }

    // hae tapahtumat kaupungin mukaan
    @GetMapping(value = "/api/events/byName")
    public ResponseEntity<List<Event>> findEventsByName(@RequestParam("name") String name) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(name);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    // hae tapahtumat tapahtuman nimen mukaan
    @GetMapping(value = "/api/events/byCity")
    public ResponseEntity<List<Event>> findEventsByCity(@RequestParam("city") String city) {
        List<Event> events = eventRepository.findByCityIgnoreCase(city);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    // Lisää tapahtuma
    @PostMapping(value = "/api/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event newEvent) {

        /*
        //This is something we can get errors from bean validations.
        //Problem is that it's not really needed. It get's overwritten somewhere.
        //Validation works fine without it and error messages are sent in response correctly.
        //We just dont have manual access to the errors.

        Set<ConstraintViolation<Event>> violations = validator.validate(newEvent);
        for (ConstraintViolation<Event> violation : violations) {
            System.out.println(violation.getMessage());
        }
        */

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventRepository.save(newEvent));
    }

    // Tapahtuman päivitys
    @PutMapping(value = "/api/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        return eventRepository.findById(id).map(event -> {
            event.setName(eventRequest.getName());
            event.setDate(eventRequest.getDate());
            event.setPlace(eventRequest.getPlace());
            event.setCity(eventRequest.getCity());
            event.setTicketCount(eventRequest.getTicketCount());

            Event updatedEvent = eventRepository.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Tapahtuman poisto
    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id) {
        return eventRepository.findById(id).map(event -> {
            eventRepository.delete(event);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // Lisää lipputyypit tapahtumaan
    @PostMapping("/api/events/{eventId}/tickettypes")
    public ResponseEntity<?> createTicketTypesForEvent(@PathVariable Long eventId,
            @Valid @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
        // Tarkastetaan löytyykö tapahtuma id:n perusteella
        return eventRepository.findById(eventId).map(event -> {
            List<TicketType> ticketTypes = new ArrayList<>();
            for (TicketTypeRequest ttRequest : ticketTypeRequests) {
                if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
                    return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
                }
                TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
                ticketTypes.add(newTicketType);
            }

            List<TicketType> savedTicketTypes = (List<TicketType>) ticketTypeRepository.saveAll(ticketTypes);

            // Yksinkertaistetaan JSON-vastausta
            List<Map<String, Object>> response = savedTicketTypes.stream().map(tt -> {
                Map<String, Object> ticketTypeResponse = new HashMap<>();
                ticketTypeResponse.put("id", tt.getId());
                ticketTypeResponse.put("name", tt.getName());
                ticketTypeResponse.put("price", tt.getPrice());
                ticketTypeResponse.put("event", tt.getEvent().getId()); // sisällytä vain ID
                return ticketTypeResponse;
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Päivität tapahtuman lipputyyppejä
    @PutMapping("/api/events/{eventId}/tickettypes")
    public ResponseEntity<?> updateTicketTypesForEvent(@PathVariable Long eventId,
                                                       @RequestBody List<TicketTypeRequest> ticketTypeRequests) {
        // Tarkastetaan löytyykö tapahtuma id:n perusteella
        return eventRepository.findById(eventId).map(event -> {
            List<TicketType> existingTicketTypes = ticketTypeRepository.findByEvent(event);

            // Tarkasta onko lipputyypeille jo luotu lippuja
            boolean areTicketTypesUsed = existingTicketTypes.stream()
                    .anyMatch(tt -> !ticketRepository.findByTicketType(tt).isEmpty());

            if (areTicketTypesUsed) {
                return ResponseEntity.badRequest().body("Cannot update ticket types as tickets have already been created.");
            }

            // Poistetaan aikaisemmat lipputyypit
            ticketTypeRepository.deleteAll(existingTicketTypes);

            // Tarkastetaan lipputyyppien nimet, luodaan uudet lipputyypit ja määritetään
            // mihin tapahtumaan ne kuuluvat
            List<TicketType> ticketTypes = new ArrayList<>();
            for (TicketTypeRequest ttRequest : ticketTypeRequests) {
                if (!ALLOWED_TICKET_TYPES.contains(ttRequest.getName())) {
                    return ResponseEntity.badRequest().body("Ticket type " + ttRequest.getName() + " is not allowed.");
                }
                TicketType newTicketType = new TicketType(ttRequest.getName(), ttRequest.getPrice(), event);
                ticketTypes.add(newTicketType);
            }

            List<TicketType> savedTicketTypes = (List<TicketType>) ticketTypeRepository.saveAll(ticketTypes);

            // Yksinkertaistetaan JSON-vastausta
            List<Map<String, Object>> response = savedTicketTypes.stream().map(tt -> {
                Map<String, Object> ticketTypeResponse = new HashMap<>();
                ticketTypeResponse.put("id", tt.getId());
                ticketTypeResponse.put("name", tt.getName());
                ticketTypeResponse.put("price", tt.getPrice());
                ticketTypeResponse.put("event", tt.getEvent().getId()); // Sisällytetään vastaukseen vain tapahtuman id
                return ticketTypeResponse;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

}
