package eu.concept.repository.concept.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceConceptProperties;
import eu.concept.main.Application;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.UserCo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@EnableConfigurationProperties(DatasourceConceptProperties.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DatasourceConceptConfig.class})

public class BriefAnalysisServiceTest {

    @Autowired
    BriefAnalysisService baService;

    @Test
//    @Ignore
    public void testFetchFiles() {
        int projectID = 2;
        String role = "MANAGER";
        List<BriefAnalysis> baFiles = baService.fetchBriefAnalysisByProjectId(projectID, new UserCo(191, role), 10);
        for (BriefAnalysis ba : baFiles) {
            Logger.getLogger(BriefAnalysisServiceTest.class.getName()).log(Level.INFO, "BID: {0} Title: {1} IsPublic: {2}", new Object[]{ba.getId(), ba.getTitle(), ba.getIsPublic()});
        }

    }

}
