package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Sketch;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface SketchRepository extends JpaRepository<Sketch, Integer> {

    /*
     *Ftech Queries
     */
    public List<Sketch> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<Sketch> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public Sketch findById(int Id);

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
