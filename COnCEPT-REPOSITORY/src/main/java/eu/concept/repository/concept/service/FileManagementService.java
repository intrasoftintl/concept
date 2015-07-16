package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.FileManagementRepository;
import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<FileManagement> fetchImagesByProjectIdAndUserId(int projectID, String userRole) {
        List<FileManagement> files;
        if ("CLIENT".equals(userRole)) {
            files = fileManagement.findByPidAndIsPublic(projectID, new Short("1"));
        } else {
            files = fileManagement.findByPid(projectID);
        }
        return files;
    }

    public FileManagement fetchImageById(int id){
        return fileManagement.findById(id);
    }
    
}
