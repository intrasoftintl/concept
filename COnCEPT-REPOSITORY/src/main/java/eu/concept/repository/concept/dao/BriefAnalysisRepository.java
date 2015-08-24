package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.BriefAnalysis;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface BriefAnalysisRepository extends JpaRepository<BriefAnalysis, Integer> {

    /*
     *Ftech Queries
     */

    public List<BriefAnalysis> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<BriefAnalysis> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public BriefAnalysis findById(int Id);

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
