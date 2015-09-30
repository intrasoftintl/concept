package eu.concept.repository.concept.service;

import eu.concept.main.Application;
import eu.concept.configuration.DatasourceConceptConfig;
import eu.concept.configuration.DatasourceOpenprojectConfig;
import eu.concept.repository.concept.dao.UserCoRepository;
import eu.concept.repository.concept.domain.UserCo;
import java.util.Calendar;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

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
    UserCoRepository userCoRepository;

    private final UserCo user = new UserCo(1, "junit@test.case", "John", "Smith", "password", "testUser", Calendar.getInstance().getTime());

    @Test
    @Ignore
    public void testCreateUser() {
        Assert.notNull(userCoRepository.save(user), "User " + user.getUsername() + " has not been created.");
    }

    @Test
    @Ignore
    public void testFindByUsername() {
        Assert.notNull(userCoService.findByUsername(user.getUsername()), "User with username " + user.getUsername() + " not found");
    }

    @Ignore
    @Test
    public void testFindById() {
        Assert.notNull(userCoService.findById(user.getId()), "User with ID: " + user.getId() + " not found");
    }

    @Test
    @Ignore
    public void testEditUser() {
        user.setFirstName("Sarah");
        userCoRepository.save(user);
        Assert.isTrue("Sarah".equals(userCoService.findById(user.getId()).getFirstName()), "User with username " + user.getUsername() + "could not be updated.");
    }

    @Test
    @Ignore
    public void testRemoveById() {
        userCoRepository.delete(user.getId());
        Assert.isNull(userCoService.findByUsername(user.getUsername()), "User " + user.getUsername() + " has not been deleted..");
    }
}
