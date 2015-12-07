package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.MoodboardRepository;
import eu.concept.repository.concept.domain.Moodboard;
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
public class MoodboardService {

    @Autowired
    private MoodboardRepository moodboardRepo;

    public Moodboard store(Moodboard mb) {
        try {

            mb = moodboardRepo.save(mb);
        } catch (Exception ex) {
            Logger.getLogger(MoodboardService.class.getName()).severe(ex.getMessage());
            return mb;
        }
        return mb;
    }

    public boolean delete(int briefAnalysisID) {
        try {
            moodboardRepo.delete(briefAnalysisID);
        } catch (Exception ex) {
            Logger.getLogger(MoodboardService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<Moodboard> fetchMoodboardByProjectId(int projectID, UserCo user, int limit) {
        return fetchMoodboardByProjectId(projectID, user, limit, 0);
    }

    public List<Moodboard> fetchMoodboardByProjectId(int projectID, UserCo user, int limit, int page) {
        List<Moodboard> files;
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(user.getRole())) {
            files = moodboardRepo.findByPidAndIsPublicOrderByCreatedDateDesc(projectID, new Short("1"), pageRequest);
        } else {
            files = moodboardRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        }
        return files;
    }

    public Moodboard fetchMoodboardById(int id) {
        return moodboardRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return moodboardRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return moodboardRepo.countByPid(projectID);
        }
    }

    @Transactional
    public int changePublicStatus(int sk_id, short isPublic) {
        return moodboardRepo.setPublicStatus(sk_id, (short) (isPublic == 0 ? 1 : 0));
    }

}
