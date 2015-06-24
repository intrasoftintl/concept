package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.openproject.domain.MemberOp;
import java.util.List;
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
public class MemberOpServiceTest {

    private static final Logger logger = Logger.getLogger(MemberOpServiceTest.class.getName());

    @Autowired
    MemberOpService memberOpService;

    @Test
    @Ignore
    public void findUserProjectsMembershipById() {
        int userID = 191;
        List<MemberOp> members = memberOpService.fetchUserProjectsMemmbership(userID);
        if (null == members) {
            logger.severe("No memberships found for user with id: " + userID);
        } else {
            logger.info("Total membesrhips found : " + members.size());
            for (MemberOp member : members) {
                logger.info("Project ID: " + member.getProjectId());
                //                logger.info("Project ID: " + member.getProjectId() + " Username: "+member.getUser().getLogin());
            }
        }

    }

}
