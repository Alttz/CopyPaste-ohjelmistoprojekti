package copypaste.ticketguru.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {

	void saveAndFlush(AppUser user2);	

}
