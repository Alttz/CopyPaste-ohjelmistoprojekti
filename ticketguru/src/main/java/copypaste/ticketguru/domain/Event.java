package copypaste.ticketguru.domain;

import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String date;
	private String place;
	private String city;
	private String name;
	private int ticketCount;

	@OneToMany(mappedBy = "event")
	private List<Ticket> tickets;

	public Event() {
	}

	public Event(String date, String place, String city, String name, int ticketCount) {
		super();
		this.date = date;
		this.place = place;
		this.name = name;
		this.city = city;
		this.ticketCount = ticketCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlace() {return place; }

	public void setPlace(String place) {this.place = place;}

	public String getCity() { return city; }

	public void setCity(String city) { this.city = city;}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	@Override
	public String toString() {
		return "Event [id=" + id + ", date=" + date + ", name=" + name + ", place="+ place +", city="+city+", ticketCount=" + ticketCount + "]";
	}
}
