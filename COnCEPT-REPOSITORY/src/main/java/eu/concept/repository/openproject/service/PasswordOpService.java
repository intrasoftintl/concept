package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.PasswordOpRepository;
import eu.concept.repository.openproject.domain.PasswordOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public class PasswordOpService {

    @Autowired
    PasswordOpRepository userPasswordDAO;

    public void storeUserPassword(PasswordOp userPassword) {
        userPasswordDAO.save(userPassword);
    }

}
