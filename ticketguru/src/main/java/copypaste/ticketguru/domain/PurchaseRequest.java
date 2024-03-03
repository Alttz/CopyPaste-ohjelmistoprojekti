package copypaste.ticketguru.domain;

import java.util.List;

public class PurchaseRequest {
	private Long eventId;
    private Long userId; 
    private List<String> ticketTypeNames; 

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getTicketTypeNames() {
        return ticketTypeNames;
    }

    public void setTicketTypeNames(List<String> ticketTypeNames) {
        this.ticketTypeNames = ticketTypeNames;
    }


	

}
