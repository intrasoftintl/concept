package eu.concept.controller.component;

import eu.concept.configuration.COnCEPTProperties;
import eu.concept.controller.ElasticSearchController;
import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
import eu.concept.util.other.Util;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
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

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class FileManagementController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    FileManagementService fmService;

    @Autowired
    COnCEPTProperties conceptProperties;

    @Autowired
    NotificationService notificationService;


    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/fm_app", method = RequestMethod.GET)
    public String fm_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm_app";
    }

    //Fetch an image
    @RequestMapping(value = "/file/{image_id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") int imageId, @RequestParam(value = "preview", defaultValue = "0", required = false) int preview) throws IOException {
        FileManagement fm = fmService.fetchImageById(imageId);
        byte[] imageContent;
        final HttpHeaders headers = new HttpHeaders();
        MediaType fileType = MediaType.valueOf(fm.getType());
        imageContent = DatatypeConverter.parseBase64Binary(fm.getContent().replaceAll("data:".concat(fm.getType()).concat(";base64,"), ""));
        headers.setContentType(fileType);

        if (!(fm.getType().equals(MediaType.IMAGE_PNG_VALUE) || fm.getType().equals(MediaType.IMAGE_GIF_VALUE) || fm.getType().equals("image/jpeg")) && preview == 1) {
            URL genericImageURL = new URL(conceptProperties.getFMGenericImageURL());
            BufferedImage image = ImageIO.read(genericImageURL);
            // write image to outputstream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            // get bytes
            imageContent = baos.toByteArray();
            headers.setContentType(MediaType.IMAGE_PNG);
        }

        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/fm_app/{image_id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getImageRedirect(@PathVariable("image_id") int imageId, @RequestParam(value = "preview", defaultValue = "0", required = false) int preview) throws IOException {
        return getImage(imageId, preview);
    }

    @RequestMapping(value = "/filemanagement/{project_id}", method = RequestMethod.GET)
    public String fetchFilesByProjectID(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("fmContents", fmService.fetchImagesByProjectIdAndUserId(project_id, WebController.getCurrentRole(), limit));
        model.addAttribute("totalFiles", fmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm :: fmContentList";
    }

    @RequestMapping(value = "/filemanagement_all/{project_id}", method = RequestMethod.GET)
    public String fetchFilesByProjectIDAll(Model model, @PathVariable int project_id, @RequestParam(value = "limit", defaultValue = "0", required = false) int limit) {
        model.addAttribute("fmContents", fmService.fetchImagesByProjectIdAndUserId(project_id, WebController.getCurrentRole(), limit));
        model.addAttribute("totalFiles", fmService.countFilesById(project_id, WebController.getCurrentRole()));
        model.addAttribute("projectID", project_id);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm :: fmContentAllList";
    }

    @RequestMapping(value = "/fm", method = RequestMethod.GET)
    public String fmPage(Model model) {
        return "fm";
    }

    @RequestMapping(value = "/fm_app_delete_all", method = RequestMethod.GET)
    public String deleteFileByFM(Model model, @RequestParam(value = "fm_id", defaultValue = "0", required = false) int fm_id, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        FileManagement fm = fmService.fetchImageById(fm_id);
        if (null != fm && fmService.deleteFile(fm_id)) {
            notificationService.storeNotification(project_id, NotificationTool.FM, NotificationTool.NOTIFICATION_OPERATION.DELETED, "a file (" + fm.getFilename() + ")", conceptProperties.getFMUploadGenericImageURL(), WebController.getCurrentUserCo());
            //Delete from elastic search engine (id=component_name+ba_id)
            ElasticSearchController.getInstance().deleteById(Util.getComponentName(FileManagement.class.getSimpleName()) + String.valueOf(fm_id));
        }
        return fetchFilesByProjectIDAll(model, project_id, limit);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/fm_app", method = RequestMethod.POST)
    public String fm_appPost(Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
        model.addAttribute("projectID", projectID);
        return fm_app(model);
    }

    @RequestMapping(value = "/fm_all", method = RequestMethod.POST)
    public String fm_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm_all";
    }

}
