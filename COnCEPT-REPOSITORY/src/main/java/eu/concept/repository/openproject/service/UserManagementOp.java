package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.UserOp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public interface UserManagementOp {
 

    @Transactional
    public UserOp addUserToOpenproject(UserOp user, PasswordOp userPassword);
    
}
