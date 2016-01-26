package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.UserCoRepository;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.util.other.Util;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
@Transactional
public class UserCoService {

    private static final Logger logger = Logger.getLogger(UserCoService.class.getName());

    @Autowired
    UserCoRepository userDAO;

    public UserCo findByUsername(String username) { 
         return userDAO.findByUsername(username);
    }

    
    /**
     * Fetch a user from database , given a username and a password
     * 
     * @param username The username 
     * @param password The password 
     * @return A UserCo object (null if no user is found)
     */
    public UserCo findByUsernameAndPassword(String username, String password) {
        UserCo user;
        user = userDAO.findByUsernameAndPassword(username, Util.createAlgorithm(password, "SHA"));
        if (null == user) {
            logger.warning("User could not be found...");
        } 
        return user;
    }
    
    public UserCo findById(int id){
        return userDAO.findById(id);
    }

    
    
    
    
}
