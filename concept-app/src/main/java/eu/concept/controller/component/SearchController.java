package eu.concept.controller.component;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
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
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping(value = "/se_app", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(Model model, @ModelAttribute Search search) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        System.out.println("Project Id is: " + search.getPid());
        System.out.println("Content is: " + search.getContent());
        System.out.println("Component Id is: " + search.getComponent().getId());
        search.setCategories(ElasticSearchController.getInstance().getCategoriesNames(search.getCategories()));
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

    @RequestMapping(value = "/search_upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("project_id") String project_id, Model model) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                HttpResponse<JsonNode> response = Unirest.post("http://192.168.3.5:8081/semantic-enhancer/tags/image").body(bytes).asJson();
                JSONArray results = response.getBody().getArray();
                List<String> keywords = new ArrayList<>();
                //Iterate all keywords
                for (int i = 0; i < results.length(); i++) {
                    if (0.5 < results.getJSONObject(i).getDouble("relevancy")) {
                        keywords.add(results.getJSONObject(i).getString("name"));
                    }
                }
                //Construct Keywords Phrase
                String keywordPhrase = Joiner.on(",").join(keywords);
                System.out.println("Keyword Phrase is: "+keywordPhrase);
                List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
                model.addAttribute("projects", projects);
                model.addAttribute("projectID", project_id);
                model.addAttribute("currentUser", getCurrentUser());
                String search_query_url = constructSearchUrl(project_id, "", "", "", keywordPhrase);
                model.addAttribute("search_query_url", search_query_url);

            } catch (Exception e) {
                return "You failed to upload  => " + e.getMessage();
            }
        } else {
            //TODO: Error Handling
        }

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
