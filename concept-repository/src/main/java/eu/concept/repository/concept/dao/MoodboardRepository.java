package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Storyboard;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface StoryboardRepository extends JpaRepository<Storyboard, Integer> {

    /*
     *Ftech Queries
     */
    public List<Storyboard> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<Storyboard> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public Storyboard findById(int Id);

    /*
     * Modify Queries
     */
    @Modifying
    @Query("update Storyboard sb set sb.isPublic=?2 where sb.id=?1")
    public int setPublicStatus(int Id, short IsPublic);

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
