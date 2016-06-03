package eu.concept.controller.component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.ProjectCategory;
import eu.concept.repository.concept.service.CategoryService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.ProjectCategoryService;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

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

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserCoService userCoService;

    @Autowired
    ProjectCategoryService projectCategoryService;

    @Autowired
    CategoryService categoryService;

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
    public String fetchProjectModelByID(Model model, @RequestParam(value = "projectID") String projectID, @RequestParam(value = "initTaxonomy", required = false, defaultValue = "false") String initTaxonomy) {
        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        ProjectCategory projectCategory = projectCategoryService.findByPid(Integer.valueOf(projectID));

        //Check if initTaxonomy is set
        if ("true".equals(initTaxonomy)) {
            projectCategory.setCurrentStructure(categoryService.constructTaxonomyJSON(projectCategory.getCategories()));
            //String content = new JSONObject().put("project_id", projectID).put("categories", new JSONArray(projectCategory.getCurrentStructure())).toString();
            try {
                System.out.println("Content JSON: " + projectCategory.getCurrentStructure());
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://concept-se.euprojects.net/insert_categories").field("project_id", projectID).field("categories", projectCategory.getCurrentStructure()).asJson();
                System.out.println("Response is: " + jsonResponse.getBody());

            } catch (UnirestException ex) {
                Logger.getLogger(ProjectManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
            projectCategoryService.storeProjectCategory(projectCategory);
        }

        if (null == projectCategory) {
            model.addAttribute("showTaxonomyAlert", "true");
        } else {
            model.addAttribute("showTaxonomyAlert", null == projectCategory.getCurrentStructure() ? "true" : "false");
        }

        return "category_app";
    }

}
