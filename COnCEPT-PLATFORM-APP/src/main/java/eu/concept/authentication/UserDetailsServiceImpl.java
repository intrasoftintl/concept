package eu.concept.authentication;

import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.repository.openproject.service.PasswordOpService;
import eu.concept.repository.openproject.service.UserOpService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class.getName());
    private final UserOpService userOpService;
    private final PasswordOpService passwordService;
    private final UserCoService userCoSerivce;
    
    @Autowired
    public UserDetailsServiceImpl(UserOpService userOpService, PasswordOpService passwordService, UserCoService userCoSerivce) {
        this.userOpService = userOpService;
        this.passwordService = passwordService;
        this.userCoSerivce = userCoSerivce;
    }

    /**
     * Fetch a user from database on a username
     *
     * @param username The username to match for
     * @return An instance of UserCo object (userdetails.UserCo)
     * @throws UsernameNotFoundException
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.log(Level.INFO, "Authenticating user with username={0}", username);
        //Fetch user by username
        UserOp userOp = userOpService.findUserByUsername(username);
        //Check if user exists
        if (null == userOp) {
            logger.log(Level.WARNING, "User with username={0} has not been found to the database...", username);
            throw new UsernameNotFoundException("User with username=" + username + "has not been found to the database...");
        }
        //Ftech COnCEPT User
        logger.info("Trying to find concept user with open project UserID: " + userOp.getId() + " and username: " + userOp.getLogin());
        UserCo userConcept = userCoSerivce.findById(userOp.getId());
        if (null == userConcept) {
            logger.warning("Password not found in Concept database....");
            throw new UsernameNotFoundException("password not found");
        }
        
        logger.log(Level.INFO, "Password hash: {0} for user: {1}", new Object[]{userConcept.getPassword(), userConcept.getId()});

        //Fetch password by user id
        PasswordOp password = passwordService.findPasswordByUserID(userOp.getId());

        //Set user role
        GrantedAuthority grandedAuthority = new SimpleGrantedAuthority("all");
        List<GrantedAuthority> rolesList = new ArrayList<>();
        rolesList.add(grandedAuthority);

        //TODO: Fetch role
        User authUser = new User(userOp.getLogin(), userConcept.getPassword(), rolesList);
        return authUser;
    }
}
