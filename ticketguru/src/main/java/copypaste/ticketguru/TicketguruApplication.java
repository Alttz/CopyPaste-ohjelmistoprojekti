package copypaste.ticketguru;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;
import copypaste.ticketguru.domain.Purchase;
import copypaste.ticketguru.domain.PurchaseRepository;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;
import copypaste.ticketguru.domain.UserRepository;

@SpringBootApplication
public class TicketguruApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(PurchaseRepository prepository, EventRepository erepository, TicketRepository trepository, UserRepository urepository) {
		return (args) -> {

			Event e4 = new Event("28.9.2023","Hartwallareena","Helsinki","Lordi",1000);
			Event e5 = new Event("1.4.2024","PubiTarmo","Turku","Apulanta",1000);
			Event e6 = new Event("18.7.2024","Kansallisteatteri","Pasila","Käärijä",1000);
			Event e7 = new Event("5.5.2024","Koulun musaluokka","Luhanka","Antti Tuisku",1000);

			erepository.save(e4);
			erepository.save(e5);
			erepository.save(e6);

			Ticket t4 = new Ticket("Eläkeläinen", 10.00, e4, null, false);
			Ticket t5 = new Ticket("Opiskelija", 10.00, e4, null, false);
			Ticket t6 = new Ticket("Aikuinen", 10.00, e4, null, false);
			Ticket t7 = new Ticket("Aikuinen", 10.00, e4, null, false);

			trepository.save(t4);
			trepository.save(t5);
			trepository.save(t6);
			trepository.save(t7);
			
			AppUser u1 = new AppUser("TeppoTestaaja", null, null, null);
			
			urepository.save(u1);
			
            List<Ticket> tickets = Arrays.asList(t4, t5);
			
			Purchase p1 = new Purchase(u1, new Date(), tickets);
					
			prepository.save(p1);
		};
	}

}
