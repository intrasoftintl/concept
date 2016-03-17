package eu.concept.repository.openproject.service;

import eu.concept.main.Application;
import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Christos Paraskeva
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DatasourceConceptConfig.class, DatasourceOpenprojectConfig.class})
public class UserManagementServiceTest {

    @Autowired
    private UserManagementOp userManagementOpService;
    private static final Logger logger = Logger.getLogger(UserManagementServiceTest.class.getName());

    @Test
    @Ignore
    @Transactional
    public void testAddUserToOpenproject() {
        //Create User
        UserOp user = new UserOp();
        user.setAdmin(false);
        user.setLogin("user6");
        user.setFirstname("chris");
        user.setLastname("paraskeva");
        user.setMail("chris@test.com");
        user.setType("User");
        user.setLanguage("en");
        user.setStatus(1);
        user.setMailNotification("only_my_events");
        //Create User Password
        PasswordOp userPassword = new PasswordOp();
        userPassword.setHashedPassword("user6");
        logger.info("UserID: " + userPassword.getId() + " HashedPassword:" + userPassword.getHashedPassword() + " Salt: " + userPassword.getSalt());
        //Store User with Password to database
        //userManagementOpService.addUserToOpenproject(user, userPassword);

    }

}
