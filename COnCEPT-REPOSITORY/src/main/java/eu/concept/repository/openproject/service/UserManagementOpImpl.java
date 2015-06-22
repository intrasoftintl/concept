package eu.concept.repository.openproject.service;

import eu.concept.repository.concept.dao.UserCoRepository;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.openproject.dao.PasswordOpRepository;
import eu.concept.repository.openproject.dao.UserOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import eu.concept.util.other.Util;
import java.util.Calendar;
import java.util.logging.Level;
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

    private final UserOpRepository userOpRepository;
    private final PasswordOpRepository passwordOpRepository;
    private final UserCoRepository userCoRepository;

    @Autowired
    public UserManagementOpImpl(UserOpRepository userOpRepository, PasswordOpRepository passwordOpRepository, UserCoRepository userCoRepository) {
        this.userOpRepository = userOpRepository;
        this.passwordOpRepository = passwordOpRepository;
        this.userCoRepository = userCoRepository;
    }

    /**
     * Creates a new user to Openproject
     *
     * @param user An instance of UserOp object
     * @param password An instance of PasswordOp object
     * @return
     */
    @Transactional
    @Override
    public ApplicationResponse addUserToOpenproject(UserOp user, PasswordOp password) {
        Calendar cal = Calendar.getInstance();

        //Check user/password length
        if (user.getLogin().length() < 5 || password.getHashedPassword().length() < 5) {
            return new ApplicationResponse(BasicResponseCode.EXCEPTION, "Username/Password is too short (5 chars min)", null);
            //User/Password too short Exception
        }

        //Check if user already exists
        if (null != userOpRepository.findByLogin(user.getLogin())) {
            logger.log(Level.WARNING, "User with username: {0} already exists... aborting create new user", user.getLogin());
            return new ApplicationResponse(BasicResponseCode.EXCEPTION, "User with username: " + user.getLogin() + " already exists..", null);
            //User already exists Exception
        }
        //Store UserCo to database
        user.setType("User");
        user.setLanguage("en");
        user.setStatus(1);
        user.setMailNotification("only_my_events");
        UserOp addedUser = userOpRepository.save(user);
        //Store password to database
        String password_sha1 = Util.createAlgorithm(password.getHashedPassword(), "SHA");
        String salt = Util.getRandomHexString(32);
        String password_sha_salt = Util.createAlgorithm(salt + password_sha1, "SHA");
        password.setUserId(addedUser.getId());
        password.setHashedPassword(password_sha_salt);
        password.setSalt(salt);
        password.setCreatedAt(cal.getTime());
        password.setUpdatedAt(cal.getTime());
        //Store Password to database
        passwordOpRepository.save(password);
        //Store UserCo to Concept Database
        UserCo conceptUser = new UserCo(addedUser.getId(), addedUser.getMail(), addedUser.getFirstname(), addedUser.getLastname(), password_sha1, addedUser.getLogin(),cal.getTime());
        userCoRepository.save(conceptUser);
        return new ApplicationResponse(BasicResponseCode.SUCCESS, "User with username: " + user.getLogin() + "has been successfully created!", null);
    }

}
