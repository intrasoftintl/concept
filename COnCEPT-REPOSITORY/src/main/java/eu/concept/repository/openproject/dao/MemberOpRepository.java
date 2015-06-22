package eu.concept.repository.openproject.dao;

import eu.concept.repository.openproject.domain.MemberOp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva
 */
public interface MemberOpRepository extends JpaRepository<MemberOp, Integer> {

    @Query("select m from MemberOp m where m.userId=?1 and m.projectId !=1")
    List<MemberOp> findByUserId(int userID);

}
