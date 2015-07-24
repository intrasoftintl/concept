package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Sketch;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.concept.service.SketchService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
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
        model.addAttribute("skContents", skService.fetchBriefAnalysisByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", skService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "sk :: skContentList";
    }
    
    @RequestMapping(value = "/sketches_all/{project_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("skContents", skService.fetchBriefAnalysisByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", skService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "sk :: skContentAllList";
    }


    //TODO: Show succes/error message on save
    @RequestMapping(value = "/sk_app/{sk_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByID(Model model, @PathVariable int sk_id) {

        Sketch sk = skService.fetchBriefAnalysisById(sk_id);
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
    public String createBriefAnalysis(@ModelAttribute Sketch sk, Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
//      ApplicationResponse appResponse = userManagementService.addUserToOpenproject(user, password);
        System.out.println("ProjectID : " + sk.getPid());
        UserCo newUser = new UserCo();
        newUser.setId(getCurrentUser().getId());
        if (null == sk.getTitle() || sk.getTitle().isEmpty() ){
            sk.setTitle("Untitled");
        }
        sk.setPid(projectID);
        sk.setUid(newUser);
        model.addAttribute("sketch", sk);
        if (skService.storeFile(sk)) {
            model.addAttribute("success", "Saved to Concept DB");
        }
        return "redirect:/sk_app/" + sk.getId();
    }

}
