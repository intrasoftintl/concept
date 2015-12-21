package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Category;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author smantzouratos
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public Category findByName(String name);

    /**
     *
     * @param categoryID
     * @return
     */
    public List<Category> findByParentID(Integer categoryID);

    @Query("SELECT c FROM Category c WHERE c.parentID IS NOT NULL AND c.name LIKE %?1%")
    public List<Category> findFirst10ByNameOrderByNameAsc(String name);

    @Query("SELECT c FROM Category c WHERE c.parentID IS NOT NULL")
    public List<Category> findAllCustom(Pageable page);
}
