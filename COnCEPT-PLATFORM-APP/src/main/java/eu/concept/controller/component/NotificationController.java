package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class NotificationController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    NotificationService notificationService;

    
    
    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/notifications_app", method = RequestMethod.GET)
    public String notifcations_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "notifications_app";
    }


    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/notifications_app", method = RequestMethod.POST)
    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        model.addAttribute("projectID", projectID);
        model.addAttribute("nfContents", notificationService.fetchNotificationsByProjectId(projectID, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalNotifications", notificationService.countNotificationsById(projectID));
        return notifcations_app(model);
    }

}
