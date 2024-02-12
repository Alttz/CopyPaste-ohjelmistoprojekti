// Event.java
package copypaste.ticketguru.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String date;
	private String name;
	private int ticketCount;

	@OneToMany(mappedBy = "event")
	private List<Ticket> tickets;

	public Event() {
	}

	public Event(String date, String name, int ticketCount) {
		this.date = date;
		this.name = name;
		this.ticketCount = ticketCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", date=" + date + ", name=" + name + ", ticketCount=" + ticketCount + "]";
	}
}
