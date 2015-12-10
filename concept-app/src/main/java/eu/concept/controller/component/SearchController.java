package eu.concept.controller.component;

import eu.concept.configuration.COnCEPTProperties;
import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.Search;
import eu.concept.repository.concept.service.SearchService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.util.other.NotificationTool;
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
public class SearchController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    SearchService seService;

    @Autowired
    COnCEPTProperties conceptProperties;

    @Autowired
    NotificationService notificationService;


    /*
     *  GET Methods 
     */

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "se :: seContentList";
    }
    
    // Preferences
    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public String preferences(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "preferences";
    }
    /*
     *  POST Methods 
     */

//    @RequestMapping(value = "/fm_app", method = RequestMethod.POST)
//    public String fm_appPost(Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
//        model.addAttribute("projectID", projectID);
//        return fm_app(model);
//    }
//
//    @RequestMapping(value = "/fm_all", method = RequestMethod.POST)
//    public String fm_all(Model model) {
//        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
//        model.addAttribute("projects", projects);
//        model.addAttribute("currentUser", getCurrentUser());
//        return "fm_all";
//    }

}
