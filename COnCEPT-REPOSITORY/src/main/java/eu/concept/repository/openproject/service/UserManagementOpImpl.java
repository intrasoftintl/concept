package eu.concept.repository.openproject.service;


import eu.concept.repository.openproject.dao.PasswordOpRepository;
import eu.concept.repository.openproject.dao.UserOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.util.other.Util;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public class UserManagementOpImpl implements UserManagementOp {

    private static final Logger logger = Logger.getLogger(UserManagementOpImpl.class.getName());

    private final UserOpRepository userRepository;
    private final PasswordOpRepository passwordRepository;

    @PersistenceContext(unitName = "openprojectEntityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    public UserManagementOpImpl(UserOpRepository userRepository, PasswordOpRepository passwordRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    @Autowired
    private PasswordOpService passwordService;

    @Autowired
    private UserOpService userService;

    /**
     *
     * @param user
     * @param userPassword
     * @return
     */
    //@Transactional(rollbackFor = ConstraintViolationException.class)
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Transactional
    @Override
    public UserOp addUserToOpenproject(UserOp user, PasswordOp userPassword) {
        //TODO: Validate User data
        //Store User to database

        //try {
        user.setType("User");
        user.setLanguage("en");
        user.setStatus(1);
        user.setMailNotification("only_my_events");
        //UserOp addedUser = userRepository.save(user);
        //userService.storeUser(user);
        userRepository.save(user);
        //TODO: Validate Password data
        String password_sha1 = Util.createAlgorithm(userPassword.getHashedPassword(), "SHA");
        String salt = Util.getRandomHexString(32);
        String password_sha_salt = Util.createAlgorithm(salt + password_sha1, "SHA");
        //userPassword.setUserId(addedUser.getId());
        userPassword.setUserId(100);
        userPassword.setHashedPassword(password_sha_salt);
        userPassword.setSalt(salt);
        Calendar cal = Calendar.getInstance();
        //userPassword.setCreatedAt(cal.getTime());
        userPassword.setUpdatedAt(cal.getTime());
            //Store Password to database

        //passwordService.storeUserPassword(userPassword);
        passwordRepository.save(userPassword);
//        } catch (ConstraintViolationException e) {
//            logger.severe("\n\n\tConstraintViolationException has occured, rolling back....\n\n\n");
//            entityManager.flush();
//        }
        return null;
    }

}
