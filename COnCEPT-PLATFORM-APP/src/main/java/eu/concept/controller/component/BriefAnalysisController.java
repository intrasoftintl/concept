package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.concept.service.MetadataService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    MetadataService metadataService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/briefanalysis/{project_id}", method = RequestMethod.GET)
    public String fetchBAByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("baContents", baService.fetchBriefAnalysisByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", baService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "ba :: baContentList";
    }

    @RequestMapping(value = "/briefanalysis_all/{project_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("baContents", baService.fetchBriefAnalysisByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", baService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "ba :: baContentAllList";
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
            model.addAttribute("briefanalysis", new BriefAnalysis());
        } else {

        }
        return "ba_app";
    }

    @RequestMapping(value = "/ba", method = RequestMethod.GET)
    public String baPage(Model model) {
        return "ba";
    }

    @RequestMapping(value = "/ba_app_delete", method = RequestMethod.GET)
    public String deleteBriefAnalysisByID(Model model, @RequestParam(value = "ba_id", defaultValue = "0", required = false) int ba_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "5", required = false) int limit) {
        baService.deleteBriefAnalysis(ba_id);
        return fetchBAByProjectID(model, projetct_id, limit);
    }

    @RequestMapping(value = "/ba_app_delete_all", method = RequestMethod.GET)
    public String deleteBriefAnalysisAllByID(Model model, @RequestParam(value = "ba_id", defaultValue = "0", required = false) int ba_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        baService.deleteBriefAnalysis(ba_id);
        return fetchBriefAnalysisByProjectIDAll(model, projetct_id, limit);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/ba_app", method = RequestMethod.POST)
    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
        model.addAttribute("projectID", projectID);
        return ba_app(model);
    }

    @RequestMapping(value = "/ba_app_edit", method = RequestMethod.POST)
    public String createBriefAnalysis(@ModelAttribute BriefAnalysis ba, Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID,final RedirectAttributes redirectAttributes) {
        //Create the BriefAnalysis object
        if (null == ba.getId()) {
            UserCo newUser = new UserCo();
            newUser.setId(getCurrentUser().getId());
            ba.setPid(projectID);
            ba.setUid(newUser);//care about last edit??
        }

        //Set Default title name
        if (null == ba.getTitle() || ba.getTitle().isEmpty()) {
            ba.setTitle("Untitled");
        }
        
        //Set Default content value
        if (null == ba.getContent() || ba.getContent().isEmpty()) {
            ba.setContent("");
        }        
        

        //Store BriefAnalysis to database
        if (baService.storeFile(ba)) {
           Logger.getLogger(BriefAnalysisController.class.getName()).info("Success stored message!");
            redirectAttributes.addFlashAttribute("success", "Saved to Concept DB");
        }
        model.addAttribute("briefanalysis", ba);
        return "redirect:/ba_app/" + ba.getId();
    }

    @RequestMapping(value = "/ba_all", method = RequestMethod.POST)
    public String ba_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "ba_all";
    }

}
