package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.Search;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SearchService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class SearchController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    SearchService seService;

    @Autowired
    NotificationService notificationService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/search/{project_id}", method = RequestMethod.GET)
    public String notifications(Model model, @PathVariable int project_id) {
        Search search = new Search();
        search.setPid(project_id);
        model.addAttribute("search", search);
        return "se :: seContent";
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/se_app", method = RequestMethod.POST)
    public String search(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
        System.out.println("ProjectId is: " + projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("search_query_url", "query_url");
        return "se_app";
    }
}
