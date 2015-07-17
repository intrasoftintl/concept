package eu.concept.repository.concept.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DatasourceConceptConfig.class, DatasourceOpenprojectConfig.class})
public class FileManagementServiceTest {

    @Autowired
    FileManagementService fmService;

    @Test
    @Ignore
    public void testFetchFiles() {
        int projectID = 2;
        String role = "CLIENT";
        List<FileManagement> files = fmService.fetchImagesByProjectIdAndUserId(projectID, role,0);
        for (FileManagement file : files) {
            Logger.getLogger(FileManagementServiceTest.class.getName()).log(Level.INFO, "FID: {0} Filename: {1} IsPublic: {2}", new Object[]{file.getId(), file.getFilename(), file.getIsPublic()});
        }

    }

}
