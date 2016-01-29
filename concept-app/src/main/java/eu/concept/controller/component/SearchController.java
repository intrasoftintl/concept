package eu.concept.controller.component;

import eu.concept.controller.ElasticSearchController;
import static eu.concept.controller.WebController.getCurrentUser;
import static eu.concept.main.SemanticAnnotator.getTagsForImage;
import eu.concept.repository.concept.dao.ComponentRepository;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.domain.Search;
import eu.concept.repository.concept.service.MetadataService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SearchService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.FileManagementService;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    MetadataService metadataService;

    @Autowired
    FileManagementService fmService;

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
        model.addAttribute("keywordsAll", metadataService.findAllMetadata());
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
        Logger.getLogger(SearchController.class.getName()).info("Search URL is: " + search_query_url);
        return "se_app";
    }

    @RequestMapping(value = "/search_upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("project_id") String project_id, Model model) {
        if (!file.isEmpty()) {
            String keywordPhrase = "";
            try {
                //Create TMP Image 
                String content = "data:".concat(file.getContentType().concat(";base64,").concat(Base64.getEncoder().encodeToString(file.getBytes())));
                FileManagement fm = new FileManagement(0, 0, "TO_BE_DELETED", content, file.getContentType(), new Short("0"), null);
                fm.setUid(new UserCo(99999));
                fmService.storeFile(fm);
                Logger.getLogger(SearchController.class.getName()).info("Id of image which upload is: " + fm.getId());
                keywordPhrase = getTagsForImage("file/" + String.valueOf(fm.getId()));
                //Remove tmp image
                fmService.deleteFile(fm.getId());
                //keywordPhrase = SemanticAnnotator.extractKeywordsFromImage(file.getBytes(), file.getContentType(),SemanticAnnotator.DEFAULT_RELEVANCY_THRESHOLD);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
            model.addAttribute("projects", projects);
            model.addAttribute("projectID", project_id);
            model.addAttribute("currentUser", getCurrentUser());
            String search_query_url = constructSearchUrl(project_id, "", "", "", keywordPhrase);
            model.addAttribute("search_query_url", search_query_url);
        } else {
            //TODO: Error Handling
        }

        return "se_app";
    }

    @RequestMapping(value = "/search_similar", method = RequestMethod.POST)
    public String searchFromOther(Model model, @RequestParam(name = "project_id", defaultValue = "0") String project_id, @RequestParam(name = "cid", defaultValue = "0") String cid, @RequestParam(name = "cname", defaultValue = "") String cname) {
        Metadata metadata = metadataService.fetchMetadataByCidAndComponent(Integer.valueOf(cid), cname);
        if (null != metadata) {
            Logger.getLogger(SearchController.class.getName()).info("Cid: " + cid + "Pid: " + project_id + " Cname: " + cname + " Keywords: " + metadata.getKeywords());
        }
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        String search_query_url = constructSearchUrl(project_id, "", "", "", (null == metadata.getKeywords() ? "" : metadata.getKeywords()));
        model.addAttribute("search_query_url", search_query_url);
        return "se_app";
    }

    @RequestMapping(value = "/search_external", method = RequestMethod.GET)
    public String searchExternal(String project_id, @RequestParam(name = "cid", defaultValue = "0") String cid, @RequestParam(name = "cname", defaultValue = "") String cname, @RequestParam(name = "source", defaultValue = "") String source) {
        String url = "error";
        Metadata metadata = metadataService.fetchMetadataByCidAndComponent(Integer.valueOf(cid), cname);
        if (null != metadata && "flickr".equals(source)) {
            url = "https://www.flickr.com/search/?text=" + metadata.getKeywords();
        } else if (null != metadata && "vam".equals(source)) {
            url = "http://collections.vam.ac.uk/search/?q=" + metadata.getKeywords();
        }
        Logger.getLogger(SearchController.class.getName()).info("External Url: " + url);
        return "redirect:" + url;
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
