package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.openproject.domain.UserOp;
import java.util.logging.Logger;
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
public class UserOpServiceTest {

    private static final Logger logger = Logger.getLogger(UserManagementServiceTest.class.getName());

    @Autowired
    private UserOpService userService;

    @Test
    @Ignore
    public void findOpenprojectUser() {
        String username = "user7";
        UserOp user = userService.findUserByUsername(username);
        if (null == user) {
            logger.info("User with username: " + username + " not found!");
        } else {
            logger.info("Found User with usernmae: " + username + "  and  ID: " + user.getId());
        }

    }

}
