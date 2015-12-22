package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.ProjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author smantzouratos
 */
public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public ProjectCategory findByName(String name);

    /**
     *
     * @param projectID
     * @return
     */
    public ProjectCategory findByPid(Integer projectID);

}
