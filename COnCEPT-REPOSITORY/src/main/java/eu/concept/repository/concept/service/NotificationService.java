package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.NotificationRepository;
import eu.concept.repository.concept.domain.Notification;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.util.other.NotificationTool;
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
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepo;
    
    public boolean storeNotification(int project_id, NotificationTool tool, NotificationTool.NOTIFICATION_OPERATION operation, String title, String thumbnail, UserCo user) {        
        Notification notification = new Notification(null, project_id, tool.getImageLink(), operation.toString(), title, thumbnail, null);
        notification.setUid(user);
        try {
            notificationRepo.save(notification);
        } catch (Exception ex) {
            Logger.getLogger(NotificationService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return notification.getId() > 0;
    }
    
    public List<Notification> fetchNotificationsByProjectId(int projectID, UserCo user, int limit) {
        return fetchNotificationsByProjectId(projectID, user, limit, 0);
    }
    
    public List<Notification> fetchNotificationsByProjectId(int projectID, UserCo user, int limit, int page) {
        List<Notification> notifications;
        Pageable pageRequest = new PageRequest(page, limit);
        notifications = notificationRepo.findByPidOrderByCreatedDateDesc(projectID, pageRequest);
        return notifications;
    }
    
    public long countNotificationsById(int projectID) {
        return notificationRepo.countByPid(projectID);
    }
    
}
