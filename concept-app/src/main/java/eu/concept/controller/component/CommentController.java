package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class CommentController {

//    @Autowired
//    UserManagementOp userManagementService;
    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    BriefAnalysisService baService;

    private final Logger logger = Logger.getLogger(CommentController.class.getName());

    //Comments for BA_APP
    @RequestMapping(value = "/ba_all/{pid}/{ba_id}", method = RequestMethod.GET)
    public String ba_file(Model model, @PathVariable int pid, @PathVariable int ba_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        BriefAnalysis briefAnalysis = baService.fetchBriefAnalysisById(ba_id);
        //Check if BriefAnalysis exists
        if (null == briefAnalysis) {
            logger.severe("BriefAnalysis with Id: " + ba_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", briefAnalysis);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "comment_app";
    }

}
