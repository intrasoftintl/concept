package eu.concept.controller.component;

import eu.concept.configuration.COnCEPTProperties;
import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.domain.Sketch;
import eu.concept.repository.concept.service.MindMapService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.NotificationTool.NOTIFICATION_OPERATION;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class MindMapController {

    @Autowired
    COnCEPTProperties conceptProperties;

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    MindMapService mmService;

    @Autowired
    NotificationService notificationService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/mm_all", method = RequestMethod.GET)
    public String mm_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mm_all";
    }

    @RequestMapping(value = "/mindmap/{project_id}", method = RequestMethod.GET)
    public String fetchMMByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("mmContents", mmService.fetchMindMapByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalMindMaps", mmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "mm :: mmContentList";
    }

    @RequestMapping(value = "/mindmaps_all/{project_id}", method = RequestMethod.GET)
    public String fetchMindMapsByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("mmContents", mmService.fetchMindMapByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", mmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "mm :: mmContentAllList";
    }

    //TODO: Show succes/error message on save
    @RequestMapping(value = "/mm_app/{mm_id}", method = RequestMethod.GET)
    public String fetchMindMapByID(Model model, @PathVariable int mm_id) {
//
//        Sketch sk = skService.fetchSketchById(sk_id);
//        if (null == sk) {
//            Logger.getLogger(BriefAnalysis.class.getName()).severe("Could not found Sketch with id: " + sk_id);
//            return "error";
//        }
//
//        model.addAttribute("sketch", sk);
//        model.addAttribute("projectID", sk.getPid());
//        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
//        model.addAttribute("projects", projects);
//        model.addAttribute("currentUser", getCurrentUser());
        return "sk_app";
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
    @RequestMapping(value = "/mm_app", method = RequestMethod.POST)
    public String createMindMap(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
        model.addAttribute("projectID", projectID);

        
        System.out.println("Create URL : "+conceptProperties.getmindmapcreateurl());
        String URI = conceptProperties.getmindmapcreateurl().concat(String.valueOf(WebController.getCurrentUserCo().getId())).concat("/").concat(String.valueOf(projectID));
        System.out.println("URI: " + URI);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MindMapCreateResponse> mindmapCreateResponse = restTemplate.getForEntity(URI, MindMapCreateResponse.class);
        System.out.println("MindMapDBID:    " + mindmapCreateResponse.getBody().getMindMapId());

//        return conceptProperties.getMindmapEditurl()
        return "/";
    }

    @RequestMapping(value = "/mm_app_edit", method = RequestMethod.POST)
    public String editMindMap(@ModelAttribute MindMap mm, Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, final RedirectAttributes redirectAttributes) {
        NOTIFICATION_OPERATION action = (null == mm.getId() ? NotificationTool.NOTIFICATION_OPERATION.CREATED : NotificationTool.NOTIFICATION_OPERATION.EDITED);

        //Set default title
        if (null == mm.getTitle() || mm.getTitle().isEmpty()) {
            mm.setTitle("Untitled");
        }

        //Set current user create/edit
        mm.setUserCo(WebController.getCurrentUserCo());

        //Set the current project
        if (null == mm.getId()) {
            mm.setPid(projectID);
        }

        //Success Create/Edit
//        if (skService.storeSketch(sk)) {
//            //Create a notification for current action
//            notificationService.storeNotification(projectID, NotificationTool.SK, action, "a Sketch (" + sk.getTitle() + ")", sk.getContentThumbnail(), WebController.getCurrentUserCo());
//            redirectAttributes.addFlashAttribute("success", "Sketch saved!");
//        } else {
//            redirectAttributes.addFlashAttribute("error", "Sketch couldn't be saved.");
//        }
        //Add SK object to model
        model.addAttribute("sketch", mm);

        return "redirect:/mm_app/" + mm.getId();
    }

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
