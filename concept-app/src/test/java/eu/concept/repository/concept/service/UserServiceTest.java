package eu.concept.repository.concept.service;

import eu.concept.main.Application;
import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
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
public class UserServiceTest {

    @Autowired
    UserCoService userCoService;

    @Autowired
    ProjectServiceOp service;

    @Test
    @Ignore
    public void testFindByUsername() {
        userCoService.findByUsername("user4");
    }

    @Ignore
    @Test
    public void testFetchProjectByID() {
        ProjectOp project = service.findProjectByID(1);
        Logger.getLogger(UserServiceTest.class.getName()).info("\n\n" + project.toString());
    }

}
