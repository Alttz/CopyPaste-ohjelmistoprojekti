package copypaste.ticketguru.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;


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
