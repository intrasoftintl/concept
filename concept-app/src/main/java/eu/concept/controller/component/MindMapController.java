package eu.concept.controller.component;

import eu.concept.configuration.COnCEPTProperties;
import eu.concept.controller.ElasticSearchController;
import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.service.MetadataService;
import eu.concept.repository.concept.service.MindMapService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.TimelineService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.NotificationTool.NOTIFICATION_OPERATION;
import eu.concept.util.other.Util;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class MindMapController {

    private final Logger logger = Logger.getLogger(MindMapController.class.getName());

    @Autowired
    COnCEPTProperties conceptProperties;

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    MindMapService mmService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MetadataService metadataService;

    @Autowired
    ElasticSearchController elasticSearchController;

    @Autowired
    TimelineService timelineService;


    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/mindmap/{project_id}", method = RequestMethod.GET)
    public String fetchMMByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        List<MindMap> mmContents = mmService.fetchMindMapByProjectId(project_id, getCurrentUser().getConceptUser(), limit);
        //Check if is pinned
        mmContents.forEach(mm -> {
            mm.setPinned(timelineService.isPinned(mm.getPid(), mm.getId(), new Component("MM")));
        });

        model.addAttribute("mmContents", mmContents);
        model.addAttribute("totalMindMaps", mmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUserID", WebController.getCurrentUserCo().getId());
        model.addAttribute("currentUser", getCurrentUser());
        return "mm :: mmContentList";
    }

    @RequestMapping(value = "/mindmaps_all/{project_id}", method = RequestMethod.GET)
    public String fetchMindMapsByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        List<MindMap> mmContents = mmService.fetchMindMapByProjectId(project_id, getCurrentUser().getConceptUser(), limit);
        //Check if is pinned
        mmContents.forEach(mm -> {
            mm.setPinned(timelineService.isPinned(mm.getPid(), mm.getId(), new Component("MM")));
        });

        model.addAttribute("mmContents", mmContents);
        model.addAttribute("totalFiles", mmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "mm :: mmContentAllList";
    }

    @RequestMapping(value = "/mm_app", method = RequestMethod.POST)
    public String fetchMindmapByID(Model model, @RequestParam(value = "projectID") int projectID, @RequestParam(value = "mindmapID", defaultValue = "0") int mindmapID) {
        if (mindmapID > 0) {
            model.addAttribute("mindmapURL", "http://concept-mm.euprojects.net/wisemapping/c/maps/" + mindmapID + "/" + WebController.getCurrentUserCo().getId() + "/edit");
        } else {
            String currentUserID = String.valueOf(WebController.getCurrentUserCo().getId());
            String createdMMURL = conceptProperties.getmindmapediturl();
            String URI = conceptProperties.getmindmapcreateurl().concat(currentUserID).concat("/").concat(String.valueOf(projectID));
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MindMapCreateResponse> mindmapCreateResponse = restTemplate.getForEntity(URI, MindMapCreateResponse.class);
            if (null != mindmapCreateResponse.getBody() && "OK".equals(mindmapCreateResponse.getBody().getResult())) {
                int mm_id = mindmapCreateResponse.getBody().getMindMapId();
                createdMMURL = createdMMURL.concat(String.valueOf(mindmapCreateResponse.getBody().getMindMapId()) + "/" + currentUserID + "/edit");
                //Insert mindmap to elastic search engine            
                elasticSearchController.insert(Optional.ofNullable(mmService.fetchMindMapById(mm_id)), Optional.ofNullable(metadataService.fetchMetadataByCidAndComponent(mm_id, Util.getComponentName(MindMap.class.getSimpleName()))));
                logger.info("Success created MindMap with ID: " + mindmapCreateResponse.getBody().getMindMapId());
            } else {
                logger.severe("Could not create mindmap for projectId: " + projectID);
            }
            model.addAttribute("mindmapURL", createdMMURL);
        }

        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mm_app";
    }

    @RequestMapping(value = "/mm_app/{mm_id}", method = RequestMethod.GET)
    public String fetchMindmapByIDRedirect(Model model, @PathVariable(value = "mm_id") int mm_id) {
        MindMap mm = mmService.fetchMindMapById(mm_id);
        return fetchMindmapByID(model, null == mm ? 0 : mm.getPid(), mm_id);
    }

    @RequestMapping(value = "/mm", method = RequestMethod.GET)
    public String skPage(Model model) {
        return "mm";
    }

    @RequestMapping(value = "/mm_app_delete", method = RequestMethod.GET)
    public String deleteMindMapByID(Model model, @RequestParam(value = "mm_id", defaultValue = "0", required = false) int mm_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, @RequestParam(value = "limit", defaultValue = "5", required = false) int limit) {
        MindMap mm = mmService.fetchMindMapById(mm_id);
        //On success delete & store notification to Concept db...
        if (null != mm && mmService.delete(mm_id)) {
            notificationService.storeNotification(project_id, NotificationTool.MM, NOTIFICATION_OPERATION.DELETED, "a MindMap (" + mm.getTitle() + ")", mm.getContentThumbnail(), WebController.getCurrentUserCo());
            //Delete from elastic search engine (id=component_name+mm_id)
            elasticSearchController.deleteById(Util.getComponentName(MindMap.class.getSimpleName()) + String.valueOf(mm_id));
        }
        return fetchMMByProjectID(model, project_id, limit);
    }

    @RequestMapping(value = "/mm_app_delete_all", method = RequestMethod.GET)
    public String deleteMindMapAllByID(Model model, @RequestParam(value = "mm_id", defaultValue = "0", required = false) int mm_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        mmService.delete(mm_id);
        return fetchMindMapsByProjectIDAll(model, projetct_id, limit);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/mm_all", method = RequestMethod.POST)
    public String mm_all_post(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mm_all";
    }

    @XmlRootElement(name = "conceptCreate")
    private static class MindMapCreateResponse {

        private Integer mindMapId;
        private String result;

        public Integer getMindMapId() {
            return mindMapId;
        }

        public void setMindMapId(Integer mindMapId) {
            this.mindMapId = mindMapId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }

}
