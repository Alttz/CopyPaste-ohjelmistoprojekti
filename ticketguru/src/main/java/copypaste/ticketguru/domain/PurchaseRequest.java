package copypaste.ticketguru.domain;

import java.util.List;

public class PurchaseRequest {
	private Long eventId;
    private Long userId; // Add this line to include the user ID in the request
	private List<TicketRequest> tickets;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public Long getUserId() { // Getter for userId
        return userId;
    }

    public void setUserId(Long userId) { // Setter for userId
        this.userId = userId;
    }

	public List<TicketRequest> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketRequest> tickets) {
		this.tickets = tickets;
	}

}
