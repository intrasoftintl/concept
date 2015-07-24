package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.SketchRepository;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Sketch;
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
public class SketchService {

    @Autowired
    private SketchRepository sketch;

    public boolean storeFile(Sketch sk) {
        try {
            sketch.save(sk);
        } catch (Exception ex) {
            Logger.getLogger(SketchService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return sk.getId() > 0;
    }

    public boolean deleteSketch(int sketchID) {
        try {
            sketch.delete(sketchID);
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
            sketches = sketch.findByPidAndIsPublic(projectID, new Short("1"), pageRequest);
        } else {
            sketches = sketch.findByPid(projectID, pageRequest);
        }
        return sketches;
    }

    public Sketch fetchBriefAnalysisById(int id) {
        return sketch.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return sketch.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return sketch.countByPid(projectID);
        }
    }

}
