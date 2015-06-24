package eu.concept.authentication;

import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import eu.concept.repository.openproject.dao.MemberRoleOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.repository.openproject.service.PasswordOpService;
import eu.concept.repository.openproject.service.UserOpService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MemberRoleOpRepository memberRoleOpService;

    @Autowired
    public UserDetailsServiceImpl(UserOpService userOpService, PasswordOpService passwordService, UserCoService userCoSerivce, MemberRoleOpRepository memberRoleOpService) {
        this.userOpService = userOpService;
        this.passwordService = passwordService;
        this.userCoSerivce = userCoSerivce;
        this.memberRoleOpService = memberRoleOpService;
    }

    /**
     * Fetch a user from database on a username
     *
     * @param username The username to match for
     * @return An instance of CurrentUser object
     * @throws UsernameNotFoundException
     */
    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
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
            logger.log(Level.WARNING, "Password for user: {0} not found in Concept database....", username);
            throw new UsernameNotFoundException("password not found");
        }
        
        //userConcept.
        
        //Fetch password by user id
        PasswordOp password = passwordService.findPasswordByUserID(userOp.getId());
        //Fetch user role
        MemberRoleOp memberRoleOp = memberRoleOpService.findByUserId(userOp.getId());

        return new CurrentUser(userOp.getId(), userOp.getLogin(), userConcept.getPassword(), COnCEPTRole.getCOnCEPTRole(memberRoleOp),userConcept.getFirstName(),userConcept.getLastName());
    }
}
