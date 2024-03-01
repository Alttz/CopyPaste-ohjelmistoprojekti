package copypaste.ticketguru;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;

@SpringBootApplication
public class TicketguruApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(EventRepository erepository, TicketRepository trepository) {
		return (args) -> {

			Event e4 = new Event("28.9.2023","Hartwallareena","Helsinki","Lordi",1000);
			Event e5 = new Event("1.4.2024","PubiTarmo","Turku","Apulanta",1000);
			Event e6 = new Event("18.7.2024","Kansallisteatteri","Pasila","Käärijä",1000);
			Event e7 = new Event("5.5.2024","Koulun musaluokka","Luhanka","Antti Tuisku",1000);

			erepository.save(e4);
			erepository.save(e5);
			erepository.save(e6);

			Ticket t4 = new Ticket("Eläkeläinen", 10.00, e4, false);
			Ticket t5 = new Ticket("Opiskelija", 10.00, e4, false);
			Ticket t6 = new Ticket("Aikuinen", 10.00, e4, false);
			Ticket t7 = new Ticket("Aikuinen", 10.00, e4, false);

			trepository.save(t4);
			trepository.save(t5);
			trepository.save(t6);
			trepository.save(t7);
		};
	}

}
