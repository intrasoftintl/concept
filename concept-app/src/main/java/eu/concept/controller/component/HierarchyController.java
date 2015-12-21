package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class HierarchyController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/hierarchy_app", method = RequestMethod.POST)
    public String hierarchy_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "hierarchy_app";
    }
    
}
