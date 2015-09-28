package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.MemberRoleOpRepository;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
public class MemberRoleOpService {
    
    @Autowired
    MemberRoleOpRepository memberRoleOp;
    
    
    public MemberRoleOp findByUserId(int userID){
        return memberRoleOp.findByUserId(userID);
    }
    
}
