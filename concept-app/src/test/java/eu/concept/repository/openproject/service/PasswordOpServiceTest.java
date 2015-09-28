package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.openproject.domain.PasswordOp;
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
public class PasswordOpServiceTest {
    
    private static final Logger logger = Logger.getLogger(UserManagementServiceTest.class.getName());
    
    @Autowired
    PasswordOpService passwordService;
   
    @Test
    @Ignore
    public void findPasswordByUserID() {
        int userID = 170;
        PasswordOp password = passwordService.findPasswordByUserID(userID);
        if (null == password) {
            logger.severe("Password for user with id: " + userID + " not found!");
        } else {
            logger.info("Found password: " + password.getHashedPassword() + " for user with id: " + userID);
        }
        
    }
    
}
