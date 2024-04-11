package copypaste.ticketguru;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;
import copypaste.ticketguru.domain.Purchase;
import copypaste.ticketguru.domain.PurchaseRepository;
import copypaste.ticketguru.domain.Ticket;
import copypaste.ticketguru.domain.TicketRepository;
import copypaste.ticketguru.domain.TicketType;
import copypaste.ticketguru.domain.TicketTypeRepository;
import copypaste.ticketguru.domain.UserRepository;
import io.github.cdimascio.dotenv.Dotenv; // Comment this out before pushing to main

@SpringBootApplication
public class TicketguruApplication {
	
	// Used in development. Comment this out before pushing to main /*
	static {
		Dotenv dotenv = Dotenv.load();
        System.setProperty("spring.datasource.url", dotenv.get("DATABASE_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USER"));
        System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));

    }
	// */
	
	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}

	/* @Bean
	public CommandLineRunner demo(PurchaseRepository prepository, EventRepository erepository,
			TicketRepository trepository, UserRepository urepository, TicketTypeRepository ticketTypeRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {

			 Event e4 = new Event("28.9.2023", "Hartwallareena", "Helsinki", "Lordi", 1000);
			Event e5 = new Event("1.4.2024", "PubiTarmo", "Turku", "Apulanta", 1000);
			Event e6 = new Event("18.7.2024", "Kansallisteatteri", "Helsinki", "Käärijä", 1000);
			Event e7 = new Event("5.5.2024", "Koulun musaluokka", "Luhanka", "Antti Tuisku", 1000);
	        erepository.saveAll(Arrays.asList(e4, e5, e6, e7)); // Assuming you're saving events here

			TicketType elakelainenE4 = new TicketType("Eläkeläinen", 15.00, e4);
			TicketType opiskelijaE4 = new TicketType("Opiskelija", 15.00, e4);
			TicketType aikuinenE4 = new TicketType("Aikuinen", 25.00, e4);
			ticketTypeRepository.saveAll(Arrays.asList(elakelainenE4, opiskelijaE4, aikuinenE4));
			
			TicketType elakelainenE5 = new TicketType("Eläkeläinen", 15.00, e5);
			TicketType opiskelijaE5 = new TicketType("Opiskelija", 15.00, e5);
			TicketType aikuinenE5 = new TicketType("Aikuinen", 25.00, e5);
			ticketTypeRepository.saveAll(Arrays.asList(elakelainenE5, opiskelijaE5, aikuinenE5));

			TicketType elakelainenE6 = new TicketType("Eläkeläinen", 40.00, e6);
			TicketType opiskelijaE6 = new TicketType("Opiskelija", 40.00, e6);
			TicketType aikuinenE6 = new TicketType("Aikuinen", 60.00, e6);
			ticketTypeRepository.saveAll(Arrays.asList(elakelainenE6, opiskelijaE6, aikuinenE6));

			TicketType elakelainenE7 = new TicketType("Eläkeläinen", 20.00, e7);
			TicketType opiskelijaE7 = new TicketType("Opiskelija", 20.00, e7);
			TicketType aikuinenE7 = new TicketType("Aikuinen", 35.00, e7);
			ticketTypeRepository.saveAll(Arrays.asList(elakelainenE7, opiskelijaE7, aikuinenE7));

			Ticket t4 = new Ticket(elakelainenE4, e4, null, false);
	        Ticket t5 = new Ticket(opiskelijaE4, e4, null, false);
	        Ticket t6 = new Ticket(aikuinenE4, e4, null, false);
	        Ticket t7 = new Ticket(aikuinenE4, e4, null, false);
	        trepository.saveAll(Arrays.asList(t4, t5, t6, t7));

			AppUser u1 = new AppUser("TeppoTestaaja", passwordEncoder.encode("salasana"), "ROLE_ADMIN");
			AppUser u2 = new AppUser("Masa", passwordEncoder.encode("salasana2"), "ROLE_ADMIN");
	        urepository.saveAll(Arrays.asList(u1, u2));
	        
	        if (urepository.findByUsername("admin").isEmpty()) {
	            AppUser admin = new AppUser("admin", passwordEncoder.encode("adminpass"), "ROLE_ADMIN");
	            urepository.save(admin);
	        }
	        
	        if (urepository.findByUsername("user").isEmpty()) {
	            AppUser user = new AppUser("user", passwordEncoder.encode("password"), "ROLE_USER");
	            urepository.save(user);
	        }

			List<Ticket> tickets = Arrays.asList(t4, t5);
			Purchase p1 = new Purchase(new Date(), tickets, u1);

			prepository.save(p1);

			tickets.forEach(ticket -> {
				ticket.setPurchase(p1);
				trepository.save(ticket);
			});

			List<Ticket> tickets2 = Arrays.asList(t6, t7);
			Purchase p2 = new Purchase(new Date(), tickets, u2);

			prepository.save(p2);

			tickets2.forEach(ticket -> {
				ticket.setPurchase(p2);
				trepository.save(ticket);
			}); */
		};
	


