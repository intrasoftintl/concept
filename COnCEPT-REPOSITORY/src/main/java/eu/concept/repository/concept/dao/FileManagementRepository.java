package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.FileManagement;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface FileManagementRepository extends JpaRepository<FileManagement, Integer> {

    public ArrayList<FileManagement> findByPidAndIsPublic(int Pid, short IsPublic);
    public ArrayList<FileManagement> findByPid(int Pid);

}
