package eu.concept.repository.openproject.dao;


import eu.concept.repository.openproject.domain.Project;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Integer> {

}
