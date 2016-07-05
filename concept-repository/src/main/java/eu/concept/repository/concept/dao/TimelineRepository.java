package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Timeline;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Repository
@Transactional
public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    /*
     *Fetch Queries
     */
    public List<Timeline> findByPidOrderByCreatedDateAsc(int Pid, Pageable page);

    public Timeline findByPidAndCidAndComponent(int pid, int cid, Component component);

    /*
     *Count Queries
     */
    public long countByPid(int Pid);
    
    
    public void deleteByPidAndComponent(int pid, Component component);

}
