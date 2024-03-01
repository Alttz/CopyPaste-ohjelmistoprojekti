package copypaste.ticketguru.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date purchaseDate;
    
    @OneToMany(mappedBy = "purchase")
    @JsonManagedReference
	private List<Ticket> tickets;

	public Purchase() {
		super();
	}

	public Purchase(Date purchaseDate, List<Ticket> tickets) {
		super();
		this.purchaseDate = purchaseDate;
		this.tickets = tickets;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", purchaseDate=" + purchaseDate + ", tickets=" + tickets
				+ "]";
	}

	

    
}
