package copypaste.ticketguru.domain.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String date;
	private String place;
	private String city;
	private String name;
	private int ticketCount;
	
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

	public String getCity() {return city; }

	public void setCity() {this.city = city;}

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

	@Override
	public String toString() {
		return "Event [id=" + id + ", date=" + date + ", name=" + name + ", place="+ place +", city="+city+", ticketCount=" + ticketCount + "]";
	}
}
