package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.TimelineRepository;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Timeline;
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
public class TimelineService {

    @Autowired
    private TimelineRepository timelineRepo;

    public boolean store(Timeline timeline) {

        try {
            timelineRepo.save(timeline);
        } catch (Exception ex) {
            Logger.getLogger(TimelineService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return timeline.getId() > 0;
    }

    public List<Timeline> fetchTimelineByProjectId(int projectID, UserCo user, int limit) {
        return fetchTimelineByProjectIdAndComponent(projectID, user, limit, 0);
    }

    public List<Timeline> fetchTimelineByProjectIdAndComponent(int projectID, UserCo user, int limit, int page) {
        List<Timeline> notifications;
        Pageable pageRequest = new PageRequest(page, limit);
        notifications = timelineRepo.findByPidOrderByCreatedDateAsc(projectID, pageRequest);
        return notifications;
    }

    public boolean isPinned(int pid, int cid, Component component) {
        return null == timelineRepo.findByPidAndCidAndComponent(pid, cid, component) ? false : true;
    }
    
    public Timeline findByOther(int pid, int cid, Component component) {
        return timelineRepo.findByPidAndCidAndComponent(pid, cid, component);
    }

    public long countNotificationsById(int projectID) {
        return timelineRepo.countByPid(projectID);
    }

    public boolean delete(long pinId) {
        try {
            timelineRepo.delete(pinId);
        } catch (Exception ex) {
            Logger.getLogger(MindMapService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

}
