package eu.concept.repository.openproject.service;

import eu.concept.repository.concept.dao.UserRepository;
import eu.concept.repository.openproject.dao.PasswordOpRepository;
import eu.concept.repository.openproject.dao.UserOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.util.other.Util;
import java.util.Calendar;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva
 */
@Service
public class UserManagementOpImpl implements UserManagementOp {

    private static final Logger logger = Logger.getLogger(UserManagementOpImpl.class.getName());

    private final UserOpRepository userRepository;
    private final PasswordOpRepository passwordRepository;

    @Autowired
    public UserManagementOpImpl(UserOpRepository userRepository, PasswordOpRepository passwordRepository, UserRepository userCoRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    /**
     * Creates a new user to Openproject
     *
     * @param user
     * @param userPassword
     * @return
     */
    @Transactional
    @Override
    public UserOp addUserToOpenproject(UserOp user, PasswordOp userPassword) {
        Calendar cal = Calendar.getInstance();
        //TODO: Validate User data
        //Store User to database
        user.setType("User");
        user.setLanguage("en");
        user.setStatus(1);
        user.setMailNotification("only_my_events");
        UserOp addedUser = userRepository.save(user);
        //TODO: Validate Password data
        String password_sha1 = Util.createAlgorithm(userPassword.getHashedPassword(), "SHA");
        String salt = Util.getRandomHexString(32);
        String password_sha_salt = Util.createAlgorithm(salt + password_sha1, "SHA");
        userPassword.setUserId(addedUser.getId());
        userPassword.setHashedPassword(password_sha_salt);
        userPassword.setSalt(salt);
        userPassword.setCreatedAt(cal.getTime());
        userPassword.setUpdatedAt(cal.getTime());
        //Store Password to database
        passwordRepository.save(userPassword);
        return null;
    }

}
