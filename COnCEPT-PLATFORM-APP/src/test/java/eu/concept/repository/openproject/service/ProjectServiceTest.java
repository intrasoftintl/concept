package eu.concept.repository.openproject.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.openproject.domain.ProjectOp;
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
public class ProjectServiceTest {
    
    private static final Logger logger = Logger.getLogger(ProjectServiceTest.class.getName());
    
    @Autowired
    ProjectServiceOp projectService;
    
    @Test
    @Ignore
    public void findUserProjects() {
        int userID = 191;
        List<ProjectOp> projects = projectService.findProjectsByUserId(userID);
        if (null == projects || projects.isEmpty()) {
            logger.info("No projects found for user with id: " + userID);
        } else {
            for (ProjectOp project : projects) {
                logger.info("Project ID: " + project.getId() + " Name: " + project.getName());
            }
        }
    }
    
}
