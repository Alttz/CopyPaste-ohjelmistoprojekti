package copypaste.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRowRepository extends CrudRepository<PurchaseRow, Long> {

}
