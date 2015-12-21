package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Search;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface SearchRepository extends JpaRepository<Search, Long> {

    public List<Search> findByPid(int pid);
}
