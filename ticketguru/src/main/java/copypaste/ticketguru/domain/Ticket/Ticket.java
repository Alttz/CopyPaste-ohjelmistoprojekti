package copypaste.ticketguru.domain.Ticket;

import java.util.List;
import java.util.Set;

import copypaste.ticketguru.domain.Event.Event;
import copypaste.ticketguru.domain.PurchaseRow.PurchaseRow;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    // Change this to a more appropriate type later on?
    private double price; 
    // Could be its own table
    private String type;

    @OneToMany(mappedBy = "ticket")
    private Set<PurchaseRow> purchaseRows;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<PurchaseRow> getPurchaseRows() {
        return purchaseRows;
    }

    public void setPurchaseRows(Set<PurchaseRow> purchaseRows) {
        this.purchaseRows = purchaseRows;
    }

    public Ticket(long id, Event event, double price, String type, Set<PurchaseRow> purchaseRows) {
        setId(id);
        setEvent(event);
        setPrice(price);
        setType(type);
        setPurchaseRows(purchaseRows);
    }

    public Ticket() {
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", event=" + event + ", price=" + price + ", type=" + type + ", purchaseRows="
                + purchaseRows + "]";
    }
}
