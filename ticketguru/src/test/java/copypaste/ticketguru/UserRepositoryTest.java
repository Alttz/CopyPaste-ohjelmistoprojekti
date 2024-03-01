package copypaste.ticketguru;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateReadDelete() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("testPass");
        // Assuming roles are set up correctly and added to the user if needed

        // Create and save the user
        user = userRepository.save(user);

        // Read
        assertThat(userRepository.findById(user.getUser_id())).isPresent();

        // Delete
        userRepository.delete(user);
        assertThat(userRepository.findById(user.getUser_id())).isNotPresent();
    }

    @Test
    public void testUniqueUsernameConstraint() {
        AppUser user1 = new AppUser();
        user1.setUsername("uniqueUser");
        user1.setPassword("pass1");
        userRepository.save(user1);

        AppUser user2 = new AppUser();
        user2.setUsername("uniqueUser"); // Same username as user1
        user2.setPassword("pass2");

        // This should throw an exception due to the unique constraint on the username column
        assertThrows(DataIntegrityViolationException.class, () -> {
        	userRepository.saveAndFlush(user2);
        });
    }
}
