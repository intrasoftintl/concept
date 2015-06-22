package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.MemberOpRepository;
import eu.concept.repository.openproject.domain.MemberOp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
public class MemberOpService {

    @Autowired
    MemberOpRepository memberOpRepository;

    public List<MemberOp> fetchUserProjectsMemmbership(int userID) {
        return memberOpRepository.findByUserId(userID);

    }

}
