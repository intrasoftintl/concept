package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.BriefAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface BriefAnalysisRepository extends JpaRepository<BriefAnalysis, Integer> {

//    /*
//     *Ftech Queries
//     */
//    public List<FileManagement> findByPidAndIsPublic(int Pid, short IsPublic, Pageable page);
//
//    public List<FileManagement> findByPid(int Pid, Pageable page);
//
//    public FileManagement findById(int Id);
//
//    /*
//     *Count Queries
//     */
//    public int countByPidAndIsPublic(int Pid, short IsPublic);
//
//    public int countByPid(int Pid);

}
