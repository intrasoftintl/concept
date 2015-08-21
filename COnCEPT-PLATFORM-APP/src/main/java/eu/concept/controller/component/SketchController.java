package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Notification;
import eu.concept.repository.concept.domain.Sketch;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SketchService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.NotificationTool.ACTION;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class SketchController {
    
    @Autowired
    ProjectServiceOp projectServiceOp;
    
    @Autowired
    SketchService skService;
    
    @Autowired
    NotificationService notificationService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/sk_all", method = RequestMethod.GET)
    public String sk_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sk_all";
    }
    
    @RequestMapping(value = "/sketch/{project_id}", method = RequestMethod.GET)
    public String fetchSKByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("skContents", skService.fetchSketchesByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", skService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "sk :: skContentList";
    }
    
    @RequestMapping(value = "/sketches_all/{project_id}", method = RequestMethod.GET)
    public String fetchSketchesByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("skContents", skService.fetchSketchesByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", skService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "sk :: skContentAllList";
    }

    //TODO: Show succes/error message on save
    @RequestMapping(value = "/sk_app/{sk_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByID(Model model, @PathVariable int sk_id) {
        
        Sketch sk = skService.fetchSketchById(sk_id);
        if (null == sk) {
            Logger.getLogger(BriefAnalysis.class.getName()).severe("Could not found Sketch with id: " + sk_id);
            return "error";
        }
        
        model.addAttribute("sketch", sk);
        model.addAttribute("projectID", sk.getPid());
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sk_app";
    }
    
    @RequestMapping(value = "/sk_app", method = RequestMethod.GET)
    public String sk_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        if (!model.containsAttribute("sketch")) {
            model.addAttribute("sketch", new Sketch());
        } else {
            
        }
        return "sk_app";
    }
    
    @RequestMapping(value = "/sk", method = RequestMethod.GET)
    public String skPage(Model model) {
        return "sk";
    }
    
    @RequestMapping(value = "/sk_app_delete", method = RequestMethod.GET)
    public String deleteBriefAnalysisByID(Model model, @RequestParam(value = "sk_id", defaultValue = "0", required = false) int sk_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "5", required = false) int limit) {
        skService.deleteSketch(sk_id);
        return fetchSKByProjectID(model, projetct_id, limit);
    }
    
    @RequestMapping(value = "/sk_app_delete_all", method = RequestMethod.GET)
    public String deleteBriefAnalysisAllByID(Model model, @RequestParam(value = "sk_id", defaultValue = "0", required = false) int sk_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        skService.deleteSketch(sk_id);
        return fetchSketchesByProjectIDAll(model, projetct_id, limit);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/sk_app", method = RequestMethod.POST)
    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
        model.addAttribute("projectID", projectID);
        
        System.out.println("Project is: " + projectID);
        return sk_app(model);
    }
    
    @RequestMapping(value = "/sk_app_edit", method = RequestMethod.POST)
    public String editSketch(@ModelAttribute Sketch sk, Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
        ACTION action = (null == sk.getId() ? NotificationTool.ACTION.CREATED : NotificationTool.ACTION.EDITED);
        UserCo newUser = new UserCo();
        newUser.setId(getCurrentUser().getId());
        if (null == sk.getTitle() || sk.getTitle().isEmpty()) {
            sk.setTitle("Untitled");
        }
        sk.setPid(projectID);
        sk.setUid(newUser);
        model.addAttribute("sketch", sk);

        //Success Create/Edit
        if (skService.storeSketch(sk)) {
            Notification notification = new Notification(null, projectID, NotificationTool.SK.getImageLink(), action.toString(), sk.getContentThumbnail(), null);
            notification.setUid(newUser);
            notificationService.storeNotification(notification);
            model.addAttribute("success", "Saved to Concept DB");
        }
        return "redirect:/sk_app/" + sk.getId();
    }
    
    @RequestMapping(value = "/sk_all", method = RequestMethod.POST)
    public String sk_all_post(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sk_all";
    }
    
}
