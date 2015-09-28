package eu.concept.repository.openproject.dao;

import eu.concept.repository.openproject.domain.PasswordOp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva
 */
public interface PasswordOpRepository extends JpaRepository<PasswordOp, Integer> {
    
    public PasswordOp findByuserId(int userID);
    
}
