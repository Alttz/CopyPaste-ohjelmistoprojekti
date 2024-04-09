package copypaste.ticketguru.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {
	
    Optional<AppUser> findByUsername(String username);
	void saveAndFlush(AppUser user2);	

}
