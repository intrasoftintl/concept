package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.LikesRepository;
import eu.concept.repository.concept.domain.*;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class LikesService {

    @Autowired
    LikesRepository likesRepo;

    public Likes findBriefAnalysisLike(UserCo user, BriefAnalysis briefAnalisys) {
        return likesRepo.findByUidAndBaId(user, briefAnalisys);
    }

    public Likes findFileManagementLike(UserCo user, FileManagement fileManagement) {
        return likesRepo.findByUidAndFmId(user, fileManagement);
    }

    public Likes findMindMapLike(UserCo user, MindMap mindmap) {
        return likesRepo.findByUidAndMmId(user, mindmap);
    }

    public Likes findSketchLike(UserCo user, Sketch sketch) {
        return likesRepo.findByUidAndSkId(user, sketch);
    }

    public Likes findStoryBoardLike(UserCo user, Storyboard storyboard) {
        return likesRepo.findByUidAndSbId(user, storyboard);
    }

    public Likes findMoodBoardLike(UserCo user, Moodboard moodboard) {
        return likesRepo.findByUidAndMbId(user, moodboard);
    }

    public boolean storeLikes(Likes likes) {
        try {
            likesRepo.save(likes);
        } catch (Exception ex) {
            Logger.getLogger(LikesService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return likes.getId() > 0;
    }

}
