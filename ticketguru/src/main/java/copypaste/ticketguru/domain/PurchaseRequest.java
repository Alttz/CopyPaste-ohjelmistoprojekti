package copypaste.ticketguru.domain;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequest {
    @NotNull(message = "User cannot be NULL")
    private Long userId;

    // Would be good to also have validation for ticketTypes being in the valid set, but that'd require stupid code
    @NotEmpty(message = "Request rows cannot be empty")
    @NotNull(message = "Request rows cannot be NULL")
    private List<@Valid PurchaseRequestRow> purchaseRequestRows;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<PurchaseRequestRow> getPurchaseRequestRows() {
        return this.purchaseRequestRows;
    }

    public void setPurchaseRequestRows(List<PurchaseRequestRow> rows) {
        this.purchaseRequestRows = rows;
    }
}
