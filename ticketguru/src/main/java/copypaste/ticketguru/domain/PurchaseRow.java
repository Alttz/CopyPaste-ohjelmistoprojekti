package copypaste.ticketguru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// Not sure why we're making this separate, given JPA could handle this normally
// But OK

@Entity
public class PurchaseRow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public PurchaseRow(Purchase purchase, Ticket ticket) {
        setPurchase(purchase);
        setTicket(ticket);
    }

    public PurchaseRow() {
    }

    @Override
    public String toString() {
        return "PurchaseRow [purchase=" + purchase + ", ticket=" + ticket + "]";
    }
}
