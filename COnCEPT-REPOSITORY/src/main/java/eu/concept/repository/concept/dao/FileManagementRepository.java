package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface FileManagementRepository extends JpaRepository<FileManagement, Integer> {

    /*
     *Ftech Queries
     */
    public List<FileManagement> findByPidAndIsPublicOrderByCreatedDateDesc(int Pid, short IsPublic, Pageable page);

    public List<FileManagement> findByPidOrderByCreatedDateDesc(int Pid, Pageable page);

    public FileManagement findById(int Id);
    
    
    /*
     * Modify Queries
     */
    @Modifying
    @Query("update FileManagement fm set fm.isPublic=?2 where fm.id=?1")
    public int setPublicStatus(int Id, short IsPublic);
    

    /*
     *Count Queries
     */
    public int countByPidAndIsPublic(int Pid, short IsPublic);

    public int countByPid(int Pid);

}
