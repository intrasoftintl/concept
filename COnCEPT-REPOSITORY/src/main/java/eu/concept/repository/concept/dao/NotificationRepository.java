package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Notification;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /*
     *Ftech Queries
     */

    public List<Notification> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);


    /*
     *Count Queries
     */

    public long countByPid(int Pid);

}
