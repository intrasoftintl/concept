package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.BriefAnalysisService;
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
public class BriefAnalysisController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    BriefAnalysisService baService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/ba_all", method = RequestMethod.GET)
    public String ba_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "ba_all";
    }

    @RequestMapping(value = "/briefanalysis/{project_id}", method = RequestMethod.GET)
    public String fetchBAByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("baContents", baService.fetchBriefAnalysisByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", baService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "ba :: baContentList";
    }

    //TODO: Show succes/error message on save
    @RequestMapping(value = "/ba_app/{ba_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByID(Model model, @PathVariable int ba_id) {

        BriefAnalysis ba = baService.fetchBriefAnalysisById(ba_id);
        if (null == ba) {
            Logger.getLogger(BriefAnalysis.class.getName()).severe("Could not found BriefAnalysis with id: " + ba_id);
            return "error";
        }

        model.addAttribute("briefanalysis", ba);
        model.addAttribute("projectID", ba.getPid());
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "ba_app";
    }

    @RequestMapping(value = "/ba_app", method = RequestMethod.GET)
    public String ba_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        if (!model.containsAttribute("briefanalysis")) {
            BriefAnalysis ba = new BriefAnalysis();

            model.addAttribute("briefanalysis", new BriefAnalysis());
        } else {

        }
        return "ba_app";
    }

    @RequestMapping(value = "/ba", method = RequestMethod.GET)
    public String baPage(Model model) {
        return "ba";
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/ba_app", method = RequestMethod.POST)
    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
        model.addAttribute("projectID", projectID);

        System.out.println("Project is: " + projectID);
        return ba_app(model);
    }

    @RequestMapping(value = "/ba_app_edit", method = RequestMethod.POST)
    public String createBriefAnalysis(@ModelAttribute BriefAnalysis ba, Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
//      ApplicationResponse appResponse = userManagementService.addUserToOpenproject(user, password);

        System.out.println("ProjectID : " + ba.getPid());
        UserCo newUser = new UserCo();
        newUser.setId(getCurrentUser().getId());
                if (null == ba.getTitle() || ba.getTitle().isEmpty() ){
            ba.setTitle("Untitled");
        }
        ba.setPid(projectID);
        ba.setUid(newUser);
        model.addAttribute("briefanalysis", ba);
        if (baService.storeFile(ba)) {
            System.out.println("I am in!!!!!!\n" + "Saved to Concept DB");
            model.addAttribute("success", "Saved to Concept DB");

        }

        System.out.println("BA ID: " + ba.getId());

        return "redirect:/ba_app/" + ba.getId();
    }

}
