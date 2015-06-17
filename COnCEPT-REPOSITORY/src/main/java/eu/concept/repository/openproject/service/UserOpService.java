package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.UserOpRepository;
import eu.concept.repository.openproject.domain.UserOp;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
@Transactional
public class UserOpService {

    @Autowired
    private UserOpRepository userDAO;

    @Transactional
    public void storeUser(UserOp user) {
        userDAO.save(user);
    }

}
