package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Timeline;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
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

}
