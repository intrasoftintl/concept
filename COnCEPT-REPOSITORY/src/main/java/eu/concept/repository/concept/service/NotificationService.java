package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.NotificationRepository;
import eu.concept.repository.concept.domain.Notification;
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
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public boolean storeNotification(Notification notification) {
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
        notifications = notificationRepo.findByPid(projectID, pageRequest);
        return notifications;
    }

    public int countFilesById(int projectID, String userRole) {
        return notificationRepo.countByPid(projectID);
    }

}
