package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.SketchRepository;
import eu.concept.repository.concept.domain.Sketch;
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
public class SketchService {

    @Autowired
    private SketchRepository sketchRepo;

    public boolean storeSketch(Sketch sk) {
        try {
            sketchRepo.save(sk);
        } catch (Exception ex) {
            Logger.getLogger(SketchService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return sk.getId() > 0;
    }

    public boolean deleteSketch(int sketchID) {
        try {
            sketchRepo.delete(sketchID);
        } catch (Exception ex) {
            Logger.getLogger(SketchService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<Sketch> fetchSketchesByProjectId(int projectID, UserCo user, int limit) {
        return fetchSketchesByProjectId(projectID, user, limit, 0);
    }

    public List<Sketch> fetchSketchesByProjectId(int projectID, UserCo user, int limit, int page) {
        List<Sketch> sketches;
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(user.getRole())) {
            sketches = sketchRepo.findByPidAndIsPublicOrderByCreatedDateDesc(projectID, new Short("1"), pageRequest);
        } else {
            sketches = sketchRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        }
        return sketches;
    }

    public Sketch fetchSketchById(int id) {
        return sketchRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return sketchRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return sketchRepo.countByPid(projectID);
        }
    }

    @Transactional
    public int changePublicStatus(int sk_id, short isPublic) {
        return sketchRepo.setPublicStatus(sk_id, (short) (isPublic == 0 ? 1 : 0));
    }

}
