package copypaste.ticketguru.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	//@NotBlank(message="TicketType can't be blank")
    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

	//Was this deleted/moved elsewhere?
	//@DecimalMin(value = "0.0", inclusive = false)
	//private double price;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore // Ignorataan vakio Getter kun JSON vastaus luodaan
    private Event event;
    
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchase;

	//This validation has a problem. I think it's caused by the data we add from CLR?
	//@AssertTrue(message = "isUsed must be true or false")
	private boolean isUsed = false;

	public Ticket() {
	}

	public Ticket(TicketType ticketType, Event event, Purchase purchase, boolean isUsed) {
		super();
		this.ticketType = ticketType;
		this.event = event;
		this.purchase = purchase;
		this.isUsed = isUsed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	@JsonGetter("event") // palautetaan vain ID
    public Long getEventId() {
        return this.event != null ? this.event.getId() : null;
    }

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", ticketType=" + ticketType + ", event=" + event + ", purchase=" + purchase
				+ ", isUsed=" + isUsed + "]";
	}
}

