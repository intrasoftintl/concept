package eu.concept.repository.openproject.dao;


import eu.concept.repository.openproject.domain.UserOp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva
 */
public interface UserOpRepository  extends JpaRepository<UserOp, Integer>{
    
}
