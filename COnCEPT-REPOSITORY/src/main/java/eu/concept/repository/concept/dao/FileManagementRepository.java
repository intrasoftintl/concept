package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface FileManagementRepository extends JpaRepository<FileManagement, Integer> {

    public List<FileManagement> findByPidAndIsPublic(int Pid, short IsPublic);
    public List<FileManagement> findByPid(int Pid);
    public FileManagement findById(int Id);

}
