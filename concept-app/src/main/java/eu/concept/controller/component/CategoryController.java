package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import java.util.logging.Logger;
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
public class CategoryController {

    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

    @Autowired
    ProjectServiceOp projectServiceOp;

    /*
     *  GET Methods 
     */
    
    
    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/category_app", method = RequestMethod.POST)
    public String hierarchy_app(Model model, @RequestParam(value = "projectID", defaultValue = "0", required = true) String projectID) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("projectID", projectID);
        return "category_app";
    }

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public String addCategory(Model model, @RequestParam(value = "projectID", defaultValue = "", required = true) String projectID, @RequestParam(value = "categoryName", defaultValue = "", required = true) String categoryName, @RequestParam(value = "parentCategoryID", defaultValue = "", required = false) String parentCategoryID) {
        if (!model.containsAttribute("projectID")) {
            model.addAttribute("projectID", "0");
        }
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        return "category_app";
    }

}
