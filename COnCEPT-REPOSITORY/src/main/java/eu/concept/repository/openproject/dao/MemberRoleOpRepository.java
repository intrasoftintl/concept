package eu.concept.repository.openproject.dao;

import eu.concept.repository.openproject.domain.MemberRoleOp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva
 */
public interface MemberRoleOpRepository extends JpaRepository<MemberRoleOp,Integer> {
    
    @Query("select r from MemberRoleOp r,MemberOp  m where m.user.id=?1 AND m.projectId=1 AND r.memberId=m.id")
    MemberRoleOp findByUserId(int userId);
    
}
