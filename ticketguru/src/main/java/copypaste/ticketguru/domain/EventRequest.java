package copypaste.ticketguru.domain;

import java.util.List;

public class EventRequest {
	private String name;
	private String date;
	private String place;
	private String city;
	private int ticketCount;
	private List<TicketTypeRequest> ticketTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	public List<TicketTypeRequest> getTicketTypes() {
		return ticketTypes;
	}

	public void setTicketTypes(List<TicketTypeRequest> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}
}
