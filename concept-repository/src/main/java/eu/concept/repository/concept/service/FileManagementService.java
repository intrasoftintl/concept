package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.FileManagementRepository;
import eu.concept.repository.concept.domain.FileManagement;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class FileManagementService {

    @Autowired
    private FileManagementRepository fileManagementRepo;

    /**
     *
     * @param fm
     * @return
     */
    public boolean storeFile(FileManagement fm) {
        try {
            fileManagementRepo.save(fm);
        } catch (Exception ex) {
            Logger.getLogger(FileManagementService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return fm.getId() > 0;
    }

    public boolean deleteFile(int fileID) {
        try {
            fileManagementRepo.delete(fileID);
        } catch (Exception ex) {
            Logger.getLogger(FileManagementService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
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
            files = fileManagementRepo.findByPidAndIsPublicOrderByCreatedDateDesc(projectID, new Short("1"), pageRequest);
        } else {
            files = fileManagementRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        }
        files.replaceAll(fm -> {
            fm.setContent(fm.getContent().contains("image") ? fm.getContent() : "/resources/img/fm_generic.png");
            return fm;
        });
        return files;
    }

    /**
     *
     * @param id
     * @return
     */
    public FileManagement fetchImageById(int id) {
        return fileManagementRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return fileManagementRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return fileManagementRepo.countByPid(projectID);
        }
    }

    @Transactional
    public int changePublicStatus(int sk_id, short isPublic) {
        return fileManagementRepo.setPublicStatus(sk_id, (short) (isPublic == 0 ? 1 : 0));
    }

}
