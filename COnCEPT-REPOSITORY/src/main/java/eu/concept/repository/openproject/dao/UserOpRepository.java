package eu.concept.repository.openproject.dao;

import eu.concept.repository.openproject.domain.UserOp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva
 */
public interface UserOpRepository extends JpaRepository<UserOp, Integer> {

    /**
     * Finds a user in Openproject database based on a username
     *
     * @param username The username to match
     * @return An instance of UserOp object
     */
    public UserOp findByLogin(String username);

    /**
     * Finds a user in Openproject database based on a username and a password
     *
     * @param username The username to match
     * @param password The password to match
     * @return An instance of UserOp object
     */
   // public UserOp findByLoginAndPassword(String username, String password);

}
