package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.openproject.domain.MemberRoleOp;
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
public class MemberRoleOpServiceTest {

    private static final Logger logger = Logger.getLogger(MemberRoleOpServiceTest.class.getName());

    @Autowired
    MemberRoleOpService memberRoleOpService;

    @Test
    @Ignore
    public void findUserProjectsMembershipById() {
        int userID = 192;
        MemberRoleOp memberRoleOp = memberRoleOpService.findByUserId(userID);
        if (null == memberRoleOp) {
            logger.severe("No member role found for user with id: " + userID);
        } else {
            logger.info("MemberRole ID: " + memberRoleOp.getId() + " Role Id: " + memberRoleOp.getRoleId());
        }
    }

}
