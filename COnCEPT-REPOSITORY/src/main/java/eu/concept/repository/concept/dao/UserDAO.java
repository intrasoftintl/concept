package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     *
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);
}
