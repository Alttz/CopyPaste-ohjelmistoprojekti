package copypaste.ticketguru.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchase;

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

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", ticketType=" + ticketType + ", event=" + event + ", purchase=" + purchase
				+ ", isUsed=" + isUsed + "]";
	}
}

