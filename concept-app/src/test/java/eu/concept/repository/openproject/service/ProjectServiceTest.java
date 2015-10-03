package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import java.util.logging.Logger;
import org.eclipse.persistence.jpa.jpql.Assert;
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
public class ProjectServiceTest {

    private static final Logger logger = Logger.getLogger(ProjectServiceTest.class.getName());

    @Autowired
    ProjectServiceOp projectService;

    private final int ProjectID = 1;
    private final int UserID = 191;

    @Ignore
    @Test
    public void testFetchAllProjecs() {
        Assert.isNotNull(projectService.findAllProjects(), "Could not fetch projects");

    }

    @Ignore
    @Test
    public void testFetchProjectByID() {
        Assert.isNotNull(projectService.findProjectByID(ProjectID), "Could not fetch with ID: " + ProjectID);

    }

    @Test
    @Ignore
    public void findUserProjects() {
        Assert.isNotNull(projectService.findProjectsByUserId(UserID), "No projects found for user with id: " + UserID);
    }

}
