package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.PasswordOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
@Transactional
public class PasswordOpService {

    @Autowired
    PasswordOpRepository userPasswordDAO;

    @Transactional
    public void storeUserPassword(PasswordOp userPassword) {
        userPasswordDAO.save(userPassword);
    }

}
