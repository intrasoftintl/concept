package eu.concept.controller.component;

import eu.concept.controller.ElasticSearchController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.dao.ComponentRepository;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Search;
import eu.concept.repository.concept.service.MetadataService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SearchService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Autowired
    ComponentRepository componentRepo;

    @Autowired
    MetadataService metadatService;


    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/search/{project_id}", method = RequestMethod.GET)
    public String notifications(Model model, @PathVariable int project_id) {
        Search search = new Search();
        search.setPid(project_id);
        search.setComponent(new Component());
        model.addAttribute("search", search);
        model.addAttribute("components", componentRepo.findAll());
        model.addAttribute("keywordsAll", metadatService.findAllMetadata());
        return "se :: seContent";
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/se_app", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute Search search) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        System.out.println("Project Id is: " + search.getPid());
        System.out.println("Content is: " + search.getContent());
        System.out.println("Component Id is: " + search.getComponent().getId());
        search.setCategories( ElasticSearchController.getInstance().getCategoriesNames(search.getCategories()));
        System.out.println("Categories selected: " + search.getCategories());
        System.out.println("Keywords: " + search.getKeywords());
        model.addAttribute("projects", projects);
        model.addAttribute("projectID", search.getPid());
        model.addAttribute("currentUser", getCurrentUser());
        String search_query_url = constructSearchUrl(String.valueOf(search.getPid()), search.getContent(), search.getComponent().getId(), search.getCategories(), search.getKeywords());
        model.addAttribute("search_query_url", search_query_url);
        Logger.getLogger(SearchController.class.getName()).info("Serach URL is: " + search_query_url);
        return "se_app";
    }

    private String constructSearchUrl(String projectId, String content, String component, String categories, String keywords) {

        String search_query_url = "http://concept-se.euprojects.net/search_advanced?project_id=" + projectId;

        //Set content criteria
        if (!content.isEmpty()) {
            //Should title included?
            search_query_url += "&content=" + content;
        }

        //Set component criteria
        if (!component.isEmpty()) {
            search_query_url += "&component=" + component;
        }
        //Set categories criteria
        if (!categories.isEmpty()) {
            search_query_url += "&categories=" + categories;
        }

        //Set keywords criteria
        if (null != keywords) {
            search_query_url += "&keywords=" + keywords;
        }

        return search_query_url;
    }
}
