package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.UserCo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva
 */
public interface UserCoRepository extends JpaRepository<UserCo, Integer> {

    /**
     *
     * @param username
     * @return
     */
    public UserCo findByUsername(String username);

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public UserCo findByUsernameAndPassword(String username, String password);
    
    
    public UserCo findById(int id);
}
