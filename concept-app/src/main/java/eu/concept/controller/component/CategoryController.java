package eu.concept.controller.component;

import eu.concept.repository.openproject.service.ProjectServiceOp;
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

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public String addCategory(Model model, @RequestParam(value = "projectID", defaultValue = "", required = true) String projectID, @RequestParam(value = "categoryName", defaultValue = "", required = true) String categoryName, @RequestParam(value = "parentCategoryID", defaultValue = "", required = false) String parentCategoryID) {
        if (!model.containsAttribute("projectID")) {
            model.addAttribute("projectID", "0");
        }

        System.out.println("CategoryName: " + categoryName);
        System.out.println("parentCategoryID: " + parentCategoryID);
        System.out.println("projectID: " + projectID);
        
        return "hierarchy_app";
    }

}
