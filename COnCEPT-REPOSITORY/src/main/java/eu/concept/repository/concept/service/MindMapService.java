package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.MindMapRepository;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.domain.UserCo;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class MindMapService {

    @Autowired
    private MindMapRepository mindmapRepo;

    public MindMap store(MindMap mp) {
        try {

            mp = mindmapRepo.save(mp);
        } catch (Exception ex) {
            Logger.getLogger(MindMapService.class.getName()).severe(ex.getMessage());
            return mp;
        }
        return mp;
    }

    public boolean delete(int briefAnalysisID) {
        try {
            mindmapRepo.delete(briefAnalysisID);
        } catch (Exception ex) {
            Logger.getLogger(MindMapService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<MindMap> fetchMindMapByProjectId(int projectID, UserCo user, int limit) {
        return fetchMindMapByProjectId(projectID, user, limit, 0);
    }

    public List<MindMap> fetchMindMapByProjectId(int projectID, UserCo user, int limit, int page) {
        List<MindMap> files;
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(user.getRole())) {
            files = mindmapRepo.findByPidAndIsPublic(projectID, new Short("1"), pageRequest);
        } else {
            files = mindmapRepo.findByPid(projectID, pageRequest);
        }
        return files;
    }

    public MindMap fetchMindMapById(int id) {
        return mindmapRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return mindmapRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return mindmapRepo.countByPid(projectID);
        }
    }

}
