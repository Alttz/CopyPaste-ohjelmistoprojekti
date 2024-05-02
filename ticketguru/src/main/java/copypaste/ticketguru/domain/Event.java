package copypaste.ticketguru.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// Removed for now
	// @NotNull
	// @FutureOrPresent
	private LocalDate date;

	@NotBlank
	private String place;

	@NotBlank
	private String city;

	@NotBlank
	private String name;

	@Min(value = 0, message="Ticket count has to be 0 minimum")
	private int ticketCount;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<Ticket> tickets;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<TicketType> ticketTypes;

	public Event() {
	}

	public Event(LocalDate date, String place, String city, String name, int ticketCount) {
		super();
		this.date = date;
		this.place = place;
		this.name = name;
		this.city = city;
		this.ticketCount = ticketCount;
	}

	public Event(LocalDate date, String place, String city, String name, int ticketCount, List<Ticket> tickets,
			List<TicketType> ticketTypes) {
		super();
		this.date = date;
		this.place = place;
		this.city = city;
		this.name = name;
		this.ticketCount = ticketCount;
		this.tickets = tickets;
		this.ticketTypes = ticketTypes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	@JsonIgnore
	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<TicketType> getTicketTypes() {
		return ticketTypes;
	}

	public void setTicketTypes(List<TicketType> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", date=" + date + ", place=" + place + ", city=" + city + ", name=" + name
				+ ", ticketCount=" + ticketCount + ", tickets=" + tickets + ", ticketTypes=" + ticketTypes + "]";
	}

	
}
