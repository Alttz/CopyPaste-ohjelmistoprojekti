package copypaste.ticketguru.domain;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequestRow {
    @NotNull(message = "Event cannot be NULL")
	private Long eventId;

    @NotEmpty(message = "Tickets cannot be empty")
    @NotNull(message = "Tickets cannot be NULL")
    private List<@NotBlank String> ticketTypeNames;
    // Would be good to also have validation for ticketTypes being in the valid set, but that'd require stupid code

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<String> getTicketTypeNames() {
        return ticketTypeNames;
    }

    public void setTicketTypeNames(List<String> ticketTypeNames) {
        this.ticketTypeNames = ticketTypeNames;
    }
}
