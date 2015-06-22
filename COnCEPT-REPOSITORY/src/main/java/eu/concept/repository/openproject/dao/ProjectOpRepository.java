package eu.concept.repository.openproject.dao;

import eu.concept.repository.openproject.domain.ProjectOp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva
 */
public interface ProjectOpRepository extends JpaRepository<ProjectOp, Integer> {

    @Query("select p from ProjectOp p, MemberOp  m where m.userId=?1 AND m.projectId!=1 AND p.id=m.projectId")
    public List<ProjectOp> findProjectsByUserId(int userId);
}
