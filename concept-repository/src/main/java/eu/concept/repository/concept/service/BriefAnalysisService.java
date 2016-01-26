package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.BriefAnalysisRepository;
import eu.concept.repository.concept.domain.BriefAnalysis;
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
public class BriefAnalysisService {

    @Autowired
    private BriefAnalysisRepository briefAnalysisRepo;

    public boolean storeFile(BriefAnalysis ba) {
        try {
            briefAnalysisRepo.save(ba);
        } catch (Exception ex) {
            Logger.getLogger(BriefAnalysisService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return ba.getId() > 0;
    }

    public boolean deleteBriefAnalysis(int briefAnalysisID) {
        try {
            briefAnalysisRepo.delete(briefAnalysisID);
        } catch (Exception ex) {
            Logger.getLogger(BriefAnalysisService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<BriefAnalysis> fetchBriefAnalysisByProjectId(int projectID, UserCo user, int limit) {
        return fetchBriefAnalysisByProjectId(projectID, user, limit, 0);
    }

    public List<BriefAnalysis> fetchBriefAnalysisByProjectId(int projectID, UserCo user, int limit, int page) {
        List<BriefAnalysis> files;
        Pageable pageRequest = new PageRequest(page, limit);
        if ("CLIENT".equals(user.getRole())) {
            files = briefAnalysisRepo.findByPidAndIsPublicOrderByCreatedDateDesc(projectID, new Short("1"), pageRequest);
        } else {
            files = briefAnalysisRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        }
        return files;
    }

    public BriefAnalysis fetchBriefAnalysisById(int id) {
        return briefAnalysisRepo.findById(id);
    }

    public int countFilesById(int projectID, String userRole) {
        if ("CLIENT".equals(userRole)) {
            return briefAnalysisRepo.countByPidAndIsPublic(projectID, new Short("1"));
        } else {
            return briefAnalysisRepo.countByPid(projectID);
        }
    }

    @Transactional
    public int changePublicStatus(int sk_id, short isPublic) {
        return briefAnalysisRepo.setPublicStatus(sk_id, (short) (isPublic == 0 ? 1 : 0));
    }

}
