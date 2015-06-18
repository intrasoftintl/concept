package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public interface UserManagementOp {
 


    public UserOp addUserToOpenproject(UserOp user, PasswordOp userPassword);
    
}
