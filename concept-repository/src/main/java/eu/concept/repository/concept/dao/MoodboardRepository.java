package eu.concept.repository.concept.dao;
import eu.concept.repository.concept.domain.Moodboard;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface MoodboardRepository extends JpaRepository<Moodboard, Integer> {

    /*
     *Ftech Queries
     */
    public List<Moodboard> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<Moodboard> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public Moodboard findById(int Id);

    /*
     * Modify Queries
     */
    @Modifying
    @Query("update Moodboard mb set mb.isPublic=?2 where mb.id=?1")
    public int setPublicStatus(int Id, short IsPublic);

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
