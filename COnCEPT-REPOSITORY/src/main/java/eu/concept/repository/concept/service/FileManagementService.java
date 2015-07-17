package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.FileManagementRepository;
import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class FileManagementService {

    @Autowired
    private FileManagementRepository fileManagement;

    public boolean storeFile(FileManagement fm) {
        try {
            fileManagement.save(fm);
        } catch (Exception ex) {
            return false;
        }
        return fm.getId() > 0;
    }

    /**
     *
     * @param projectID
     * @param userRole
     * @param limit
     * @return
     */
    public List<FileManagement> fetchImagesByProjectIdAndUserId(int projectID, String userRole, int limit) {

        return fetchImagesByProjectIdAndUserId(projectID, userRole, limit, 0);
    }

    /**
     *
     * @param projectID
     * @param userRole
     * @param limit
     * @param page
     * @return
     */
    public List<FileManagement> fetchImagesByProjectIdAndUserId(int projectID, String userRole, int limit, int page) {
        List<FileManagement> files;
        
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(userRole)) {
            files = fileManagement.findByPidAndIsPublic(projectID, new Short("1"), pageRequest);
        } else {
            files = fileManagement.findByPid(projectID, pageRequest);
        }
        return files;
    }


    /**
     *
     * @param id
     * @return
     */
    public FileManagement fetchImageById(int id) {
        return fileManagement.findById(id);
    }

    public int countFilesByIdAndUserId(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return fileManagement.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return fileManagement.countByPid(projectID);
        }
    }

}
