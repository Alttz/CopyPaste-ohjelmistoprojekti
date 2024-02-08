package copypaste.ticketguru.domain.Purchase;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import copypaste.ticketguru.domain.Event.Event;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    Purchase findByEvent(Event event);
}
