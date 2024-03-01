package copypaste.ticketguru.domain;

import java.util.List;

public class PurchaseRequestDTO {
	
	private List<Long> tickets;

	public List<Long> getTickets() {
		return tickets;
	}

	public void setTickets(List<Long> tickets) {
		this.tickets = tickets;
	}
}
