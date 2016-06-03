    package eu.concept.controller.component;

import eu.concept.repository.concept.service.TimelineService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static eu.concept.controller.WebController.getCurrentUser;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class TimelineController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    TimelineService timelineService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/timeline_app", method = RequestMethod.GET)
    public String timeline(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "timeline_app";
    }

    /*
     *  POST Methods 
     */

    @RequestMapping(value = "/timeline_app", method = RequestMethod.POST)
    public String timeline(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("projectID", projectID);
        model.addAttribute("tlContents", timelineService.fetchTimelineByProjectId(projectID, getCurrentUser().getConceptUser(), limit));
        return "timeline_app";
    }


    /*
    @RequestMapping(value = "/notifications_app", method = RequestMethod.POST)
    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        model.addAttribute("projectID", projectID);
        model.addAttribute("nfContents", notificationService.fetchNotificationsByProjectId(projectID, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalNotifications", notificationService.countNotificationsById(projectID));
        return notifcations_app(model);
    }
    */
}
