package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.UserRepository;
import eu.concept.repository.concept.domain.User;
import eu.concept.util.other.Util;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
@Transactional
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    UserRepository userDAO;

    public User findByUsername(String username) {
        User user;
        user = userDAO.findByUsername("admin");
        return user;
    }

    
    /**
     * Fetch a user from database , given a username and a password
     * 
     * @param username The username 
     * @param password The password 
     * @return A User object (null if no user is found)
     */
    public User findByUsernameAndPassword(String username, String password) {
        User user;
        logger.info("Trying to fetch User with username: " + username + " and password(SHA): " + Util.createAlgorithm(password, "SHA"));
        user = userDAO.findByUsernameAndPassword(username, Util.createAlgorithm(password, "SHA"));
        if (null == user) {
            logger.warning("User could not be found...");
        } else {
            logger.info("Fetched user with ID: " + user.getId());
        }
        return user;
    }
    
    public User createUser(User user){
        return null;
    }

}
