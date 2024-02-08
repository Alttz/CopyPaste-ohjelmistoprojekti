package copypaste.ticketguru;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import copypaste.ticketguru.domain.Event.Event;
import copypaste.ticketguru.domain.Event.EventRepository;

@SpringBootApplication
public class TicketguruApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(EventRepository repository) {
		return (args) -> {
			Event e1 = new Event("25.11.2024", "Apulanta", 1000);
			Event e2 = new Event("5.6.2024", "Käärijä", 2000);
			Event e3 = new Event("6.1.2025", "Antti Tuisku", 1500);
			
			repository.save(e1);
			repository.save(e2);
			repository.save(e3);
		};
	}

}
