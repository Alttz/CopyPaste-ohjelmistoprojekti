package copypaste.ticketguru.domain;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private double price;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @OneToMany(mappedBy = "ticket")
    private Set<PurchaseRow> purchaseRows;

	private boolean isUsed = false;

	public Ticket() {
	}

	public Ticket(String type, double price, Event event, boolean isUsed) {
		super();
		this.type = type;
		this.price = price;
		this.event = event;
		this.isUsed = isUsed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean getUsed() {
		return this.isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Set<PurchaseRow> getPurchaseRows() {
		return purchaseRows;
	}

	public void setPurchaseRows(Set<PurchaseRow> purchaseRows) {
		this.purchaseRows = purchaseRows;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", type=" + type + ", price=" + price + ", event=" + event + ", purchaseRows="
				+ purchaseRows + "]";
	}
}

