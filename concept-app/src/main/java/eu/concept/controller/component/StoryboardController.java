package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.Storyboard;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.StoryboardService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.NotificationTool.NOTIFICATION_OPERATION;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class StoryboardController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    StoryboardService sbService;

    @Autowired
    NotificationService notificationService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/sb_all", method = RequestMethod.GET)
    public String sb_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentUser", getCurrentUser());
        return "sb_all";
    }

    @RequestMapping(value = "/storyboard/{project_id}", method = RequestMethod.GET)
    public String fetchStoryboardByProjectId(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("sbContents", sbService.fetchStoryboardByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalStoryboards", sbService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("totalFiles", sbService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "sb :: sbContentList";
    }

    @RequestMapping(value = "/storyboards_all/{project_id}", method = RequestMethod.GET)
    public String fetchStoryboardByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("sbContents", sbService.fetchStoryboardByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalFiles", sbService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        return "sb :: sbContentAllList";
    }

    //TODO: Show succes/error message on save
    @RequestMapping(value = "/sb_app/{sb_id}", method = RequestMethod.GET)
    public String fetchBriefAnalysisByID(Model model, @PathVariable int sb_id) {

        Storyboard sb = sbService.fetchStoryboardById(sb_id);
        if (null == sb) {
            Logger.getLogger(Storyboard.class.getName()).severe("Could not found Storyboard with id: " + sb_id);
            return "error";
        }

        model.addAttribute("storyboard", sb);
        model.addAttribute("projectID", sb.getPid());
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sb_app";
    }

    //Fetch an image
    @RequestMapping(value = "/sbimage/{sb_id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("sb_id") int sbId, @RequestParam(value = "preview", defaultValue = "0", required = false) int preview) throws IOException {
        Storyboard sb = sbService.fetchStoryboardById(sbId);
        byte[] imageContent;
        final HttpHeaders headers = new HttpHeaders();
        MediaType fileType = MediaType.IMAGE_PNG;
        imageContent = DatatypeConverter.parseBase64Binary(sb.getContentThumbnail().replaceAll("data:".concat(MediaType.IMAGE_PNG_VALUE).concat(";base64,"), ""));
        headers.setContentType(fileType);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/sb", method = RequestMethod.GET)
    public String sbPage(Model model) {
        return "sb";
    }

    @RequestMapping(value = "/sb_app_delete", method = RequestMethod.GET)
    public String deleteStoryboardByID(Model model, @RequestParam(value = "sb_id", defaultValue = "0", required = false) int sb_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, @RequestParam(value = "limit", defaultValue = "5", required = false) int limit) {
        Storyboard sb = sbService.fetchStoryboardById(sb_id);
        //On success delete & store notification to Concept db...
        if (null != sb && sbService.delete(sb_id)) {
            notificationService.storeNotification(project_id, NotificationTool.SB, NOTIFICATION_OPERATION.DELETED, "a Storyboard (" + sb.getTitle() + ")", sb.getContentThumbnail(), WebController.getCurrentUserCo());
        }
        return fetchStoryboardByProjectId(model, project_id, limit);
    }

    @RequestMapping(value = "/sb_app_delete_all", method = RequestMethod.GET)
    public String deleteStoryboardAllByID(Model model, @RequestParam(value = "sb_id", defaultValue = "0", required = false) int sb_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int projetct_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        sbService.delete(sb_id);
        return fetchStoryboardByProjectIDAll(model, projetct_id, limit);
    }

    /*
     *  POST Methods 
     */

    @RequestMapping(value = "/sb_all", method = RequestMethod.POST)
    public String sb_all_post(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sb_all";
    }
    
}