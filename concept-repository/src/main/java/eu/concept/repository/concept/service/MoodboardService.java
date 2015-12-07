package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.StoryboardRepository;
import eu.concept.repository.concept.domain.Storyboard;
import eu.concept.repository.concept.domain.UserCo;
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
public class StoryboardService {

    @Autowired
    private StoryboardRepository storyboardRepo;

    public Storyboard store(Storyboard sb) {
        try {

            sb = storyboardRepo.save(sb);
        } catch (Exception ex) {
            Logger.getLogger(StoryboardService.class.getName()).severe(ex.getMessage());
            return sb;
        }
        return sb;
    }

    public boolean delete(int briefAnalysisID) {
        try {
            storyboardRepo.delete(briefAnalysisID);
        } catch (Exception ex) {
            Logger.getLogger(StoryboardService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<Storyboard> fetchStoryboardByProjectId(int projectID, UserCo user, int limit) {
        return fetchStoryboardByProjectId(projectID, user, limit, 0);
    }

    public List<Storyboard> fetchStoryboardByProjectId(int projectID, UserCo user, int limit, int page) {
        List<Storyboard> files;
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(user.getRole())) {
            files = storyboardRepo.findByPidAndIsPublicOrderByCreatedDateDesc(projectID, new Short("1"), pageRequest);
        } else {
            files = storyboardRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        }
        return files;
    }

    public Storyboard fetchStoryboardById(int id) {
        return storyboardRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return storyboardRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return storyboardRepo.countByPid(projectID);
        }
    }

    @Transactional
    public int changePublicStatus(int sk_id, short isPublic) {
        return storyboardRepo.setPublicStatus(sk_id, (short) (isPublic == 0 ? 1 : 0));
    }

}
