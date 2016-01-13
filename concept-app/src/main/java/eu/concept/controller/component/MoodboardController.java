package eu.concept.controller.component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.concept.controller.ElasticSearchController;
import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Moodboard;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.MoodboardService;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.NotificationTool.NOTIFICATION_OPERATION;
import eu.concept.util.other.Util;

import java.io.*;
import java.util.List;

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
public class MoodboardController {

    final static String STORYBOARD_REST_URL = "http://concept-sb.euprojects.net/storyboard/";

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    MoodboardService mbService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserCoService userCoService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/mb_all", method = RequestMethod.GET)
    public String mb_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mb_all";
    }

    @RequestMapping(value = "/moodboard/{project_id}", method = RequestMethod.GET)
    public String fetchMoodboardByProjectId(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("mbContents", mbService.fetchMoodboardByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("totalMoodboards", mbService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("totalFiles", mbService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "mb :: mbContentList";
    }

    @RequestMapping(value = "/moodboard_all/{project_id}", method = RequestMethod.GET)
    public String fetchMoodboardByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("mbContents", mbService.fetchMoodboardByProjectId(project_id, getCurrentUser().getConceptUser(), limit));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "mb :: mbContentAllList";
    }

    @RequestMapping(value = "/mb_app", method = RequestMethod.POST)
    public String fetchMoodboardByID(Model model, @RequestParam(value = "projectID") String projectID, @RequestParam(value = "moodboardID", defaultValue = "0") int moodboardID) {
        if (moodboardID > 0){
            model.addAttribute("moodboardURL", "http://concept-sb.euprojects.net/storyboard/moodboard/edit?pid=" + projectID + "&uid=" + WebController.getCurrentUserCo().getId() + "&idSlide=" + String.valueOf(moodboardID));
        } else {
            model.addAttribute("moodboardURL", "http://concept-sb.euprojects.net/storyboard/moodboard/new?pid=" + projectID + "&uid=" + WebController.getCurrentUserCo().getId());
        }
        
        model.addAttribute("projectID", projectID);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mb_app";
    }

    //Fetch an image
    @RequestMapping(value = "/mbimage/{mb_id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("mb_id") int mbId, @RequestParam(value = "preview", defaultValue = "0", required = false) int preview) throws IOException {
        Moodboard mb = mbService.fetchMoodboardById(mbId);
        byte[] imageContent;
        final HttpHeaders headers = new HttpHeaders();
        MediaType fileType = MediaType.IMAGE_PNG;
        imageContent = DatatypeConverter.parseBase64Binary(mb.getContentThumbnail().replaceAll("data:".concat(MediaType.IMAGE_PNG_VALUE).concat(";base64,"), ""));
        headers.setContentType(fileType);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/mb", method = RequestMethod.GET)
    public String mbPage(Model model) {
        return "mb";
    }

    @RequestMapping(value = "/mb_app_delete", method = RequestMethod.GET)
    public String deleteMoodboardByID(Model model, @RequestParam(value = "mb_id", defaultValue = "0", required = false) int mb_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, @RequestParam(value = "limit", defaultValue = "5", required = false) int limit) {
        Moodboard mb = mbService.fetchMoodboardById(mb_id);
        //On success delete & store notification to Concept db...

        try {
            HttpResponse<String> response = Unirest.post(STORYBOARD_REST_URL + "moodboard/remove")
                    .queryString("idStory", mb_id)
                    .field("uid", getCurrentUser().getId())
                    .field("pid", project_id).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (null != mb && mbService.delete(mb_id)) {
            //Add a notification
            notificationService.storeNotification(project_id, NotificationTool.SB, NOTIFICATION_OPERATION.DELETED, "a Moodboard (" + mb.getTitle() + ")", mb.getContentThumbnail(), WebController.getCurrentUserCo());
            //Delete from elastic search engine (id=component_name+mb_id)
            ElasticSearchController.getInstance().deleteById(Util.getComponentName(Moodboard.class.getSimpleName()) + String.valueOf(mb_id));
        }
        return fetchMoodboardByProjectId(model, project_id, limit);
    }

    @RequestMapping(value = "/mb_app_delete_all", method = RequestMethod.GET)
    public String deleteMoodboardAllByID(Model model, @RequestParam(value = "mb_id", defaultValue = "0", required = false) int mb_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        mbService.delete(mb_id);
        try {
            HttpResponse<String> response = Unirest.post(STORYBOARD_REST_URL + "moodboard/remove")
                    .queryString("idStory", mb_id)
                    .field("uid", getCurrentUser().getId())
                    .field("pid", project_id).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return fetchMoodboardByProjectIDAll(model, project_id, limit);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/mb_all", method = RequestMethod.POST)
    public String mb_all_post(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mb_all";
    }

}
