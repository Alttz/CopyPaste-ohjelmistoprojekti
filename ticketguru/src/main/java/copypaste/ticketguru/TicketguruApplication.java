package copypaste.ticketguru;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;

@SpringBootApplication
public class TicketguruApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(EventRepository repository) {
		return (args) -> {

			Event e4 = new Event("28.9.2023","Hartwallareena","Helsinki","Lordi",1000);
			Event e5 = new Event("1.4.2024","PubiTarmo","Turku","Apulanta",1000);
			Event e6 = new Event("18.7.2024","Kansallisteatteri","Pasila","Käärijä",1000);
			Event e7 = new Event("5.5.2024","Koulun musaluokka","Luhanka","Antti Tuisku",1000);

			repository.save(e4);
			repository.save(e5);
			repository.save(e6);
			repository.save(e7);
		};
	}

}
