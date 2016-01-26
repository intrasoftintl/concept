package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.StoryboardService;
import eu.concept.repository.concept.service.UserCoService;
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
public class ProjectManagementController {

    //final static String STORYBOARD_REST_URL = "http://concept-sb.euprojects.net/storyboard/";

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    StoryboardService sbService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserCoService userCoService;

    /*
     *  GET Methods 
     */
    
    @RequestMapping(value = "/pm_app/add", method = RequestMethod.POST)
    public String fetchProjectByID(Model model, @RequestParam(value = "projectID") String projectID) {
        
        model.addAttribute("projectmanagementURL", "http://concept-pm.euprojects.net/projects/new?parent_id=project-area");
        
        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "pm_app";
    }

    
    @RequestMapping(value = "/pm_app/details", method = RequestMethod.POST)
    public String fetchProjectDetailsByID(Model model, @RequestParam(value = "projectID") String projectID) {
        
        model.addAttribute("projectmanagementURL", "http://concept-pm.euprojects.net/projects/" + projectID);
        
        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "pm_app";
    }

    @RequestMapping(value = "/pm_app/model", method = RequestMethod.POST)
    public String fetchProjectModelByID(Model model, @RequestParam(value = "projectID") String projectID) {        
        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "category_app";
    }
    
}
