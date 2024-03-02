package copypaste.ticketguru.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Purchase {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Date purchaseDate;

	@OneToMany(mappedBy = "purchase")
	@JsonManagedReference
	private List<Ticket> tickets;

	@ManyToOne
	@JoinColumn(name = "appuser_id")
	private AppUser appUser;

	public Purchase() {
		super();
	}

	public Purchase(Date purchaseDate, List<Ticket> tickets) {
		super();
		this.purchaseDate = purchaseDate;
		this.tickets = tickets;
	}

	public Purchase(Date purchaseDate, List<Ticket> tickets, AppUser appUser) {
		super();
		this.purchaseDate = purchaseDate;
		this.tickets = tickets;
		this.appUser = appUser;
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
	
	public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "Purchase [id=" + id + ", appUser=" + appUser + ", purchaseDate=" + purchaseDate + ", tickets=" + tickets + "]";
    }
}
