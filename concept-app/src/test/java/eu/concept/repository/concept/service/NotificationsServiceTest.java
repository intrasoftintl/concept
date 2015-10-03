package eu.concept.repository.concept.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.main.Application;
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
public class NotificationsServiceTest {

    @Autowired
    NotificationService notificationsService;

    @Test
    @Ignore
    public void testCreateNotification() {

    }

    @Test
    @Ignore
    public void testFindNotificationById() {

    }

    @Test
    @Ignore
    public void testRemovehById() {

    }

}
