package copypaste.ticketguru.domain;

import java.util.List;

public class PurchaseRequest {
	private Long eventId;
	private List<TicketRequest> tickets;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public List<TicketRequest> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketRequest> tickets) {
		this.tickets = tickets;
	}

}
