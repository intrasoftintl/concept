package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Sketch;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SketchService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class SearchController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    SketchService skService;

    @Autowired
    NotificationService notificationService;

    /*
     *  GET Methods 
     */

    
    @RequestMapping(value = "/se_app", method = RequestMethod.GET)
    public String search(Model model) {
            model.addAttribute("currentUser", getCurrentUser());
        return "se_app";
    }
}
