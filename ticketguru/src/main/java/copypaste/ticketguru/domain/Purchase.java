package copypaste.ticketguru.domain;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    private AppUser user;

    private Date purchaseDate;

    @OneToMany(mappedBy = "purchase")
    private Set<PurchaseRow> purchaseRows;
    
	public Purchase() {
	}

	public Purchase(AppUser user, Date purchaseDate, Set<PurchaseRow> purchaseRows) {
		this.user = user;
		this.purchaseDate = purchaseDate;
		this.purchaseRows = purchaseRows;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Set<PurchaseRow> getPurchaseRows() {
		return purchaseRows;
	}

	public void setPurchaseRows(Set<PurchaseRow> purchaseRows) {
		this.purchaseRows = purchaseRows;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", user=" + user + ", purchaseDate=" + purchaseDate + ", purchaseRows="
				+ purchaseRows + "]";
	}

    
}
