package copypaste.ticketguru.domain.Purchase;

import java.util.Date;
import java.util.Set;

import copypaste.ticketguru.domain.PurchaseRow.PurchaseRow;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    // PLACEHOLDER FOR USER OBJECT
    private long userTemp;

    private Date purchaseDate;

    @OneToMany(mappedBy = "purchase")
    private Set<PurchaseRow> purchaseRows;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser() {
        return userTemp;
    }

    public void setUser(long userId) {
        this.userTemp = userId;
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

    public Purchase(long id, long userId, Date purchaseDate, Set<PurchaseRow> purchaseRows) {
        setId(id);
        setUser(userId);
        setPurchaseDate(purchaseDate);
        setPurchaseRows(purchaseRows);
    }

    public Purchase() {
    }

    @Override
    public String toString() {
        return "Purchase [id=" + id + ", user=" + userTemp + ", purchaseDate=" + purchaseDate + ", purchaseRows="
                + purchaseRows + "]";
    }
}
