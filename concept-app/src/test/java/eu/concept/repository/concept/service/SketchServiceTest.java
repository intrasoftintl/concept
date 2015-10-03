package eu.concept.repository.concept.service;

import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceConceptProperties;
import eu.concept.main.Application;
import org.junit.Ignore;
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
public class SketchServiceTest {

    @Autowired
    SketchService sketchService;

    @Test
    @Ignore
    public void testCreateSketch() {

    }

    @Test
    @Ignore
    public void testFindSketchById() {

    }

    @Test
    @Ignore
    public void testEditSketchById() {

    }

    @Test
    @Ignore
    public void testRemoveSketchById() {

    }

}
