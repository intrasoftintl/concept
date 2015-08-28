package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.MindMap;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface MindMapRepository extends JpaRepository<MindMap, Integer> {

    /*
     *Ftech Queries
     */
    public List<MindMap> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<MindMap> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public MindMap findById(int Id);

    /*
     * Modify Queries
     */
    @Modifying
    @Query("update MindMap mm set mm.isPublic=?2 where mm.id=?1")
    public int setPublicStatus(int Id, short IsPublic);

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
