package eu.concept.controller;

import eu.concept.authentication.CurrentUser;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.repository.openproject.service.UserManagementOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Christos Paraskeva
 */
@Controller
public class WebController {
    
    private static final Logger logger = Logger.getLogger(WebController.class.getName());
    
    @Autowired
    UserManagementOp userManagementService;
    
    @Autowired
    ProjectServiceOp projectServiceOp;
    
    @Autowired
    FileManagementService fmService;


    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new UserCo());
        return "login";
    }

    // Error
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset() {
        return "reset";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model model) {
        model.addAttribute("user", new UserOp());
        model.addAttribute("password", new PasswordOp());
        return "registration";
    }

    //@PreAuthorize("hasAnyRole('DESIGNER','MANAGER','CLIENT')")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        //,@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID
        logger.log(Level.INFO, "Success login for user: {0} , with userID: {1} and role: {2}", new Object[]{getCurrentUser().getUsername(), getCurrentUser().getId(), getCurrentUser().getRole()});
        int projectID = (model.containsAttribute("projectID") ? (Integer) model.asMap().get("projectID") : 0);
        if (projectID > 0) {
            logger.info("Trying to load dashboard content for projectID: " + projectID);
            List<FileManagement> fm_content = fmService.fetchImagesByProjectIdAndUserId(projectID, getCurrentUser().getRole());
            model.addAttribute("numOfFiles", fm_content.size());
            model.addAttribute("fm_content", fm_content);
            logger.info("Files Size: " + fm_content.size());
        } else {
            logger.info("Invalid projectID: " + projectID);
        }
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "dashboard";
    }

    // Error
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }

    // Notifications
    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public String notifications(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "notifications";
    }

    // Brief Analysis APP + ALL
    @RequestMapping(value = "/ba_app", method = RequestMethod.GET)
    public String ba_app() {
        return "ba_app";
    }
    
    @RequestMapping(value = "/ba_all", method = RequestMethod.GET)
    public String ba_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "ba_all";
    }

    // File Management APP + ALL
    @RequestMapping(value = "/fm_app", method = RequestMethod.GET)
    public String fm_app(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm_app";
    }
    
    @RequestMapping(value = "/fm_all", method = RequestMethod.GET)
    public String fm_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "fm_all";
    }

    // Search Engine ALL
    @RequestMapping(value = "/se_all", method = RequestMethod.GET)
    public String se_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "se_all";
    }

    // Mindmaps ALL
    @RequestMapping(value = "/mm_all", method = RequestMethod.GET)
    public String mm_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "mm_all";
    }

    // Storyboards ALL
    @RequestMapping(value = "/sb_all", method = RequestMethod.GET)
    public String sb_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sb_all";
    }

    // Sketches APP + ALL
    @RequestMapping(value = "/sk_app", method = RequestMethod.GET)
    public String sk_app() {
        return "sk_app";
    }
    
    @RequestMapping(value = "/sk_all", method = RequestMethod.GET)
    public String sk_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "sk_all";
    }

    // Metadata APP
    @RequestMapping(value = "/md_app", method = RequestMethod.GET)
    public String md_app() {
        return "md_app";
    }

    //Fetch an image
    @RequestMapping(value = "/image/{image_id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") int imageId) throws IOException {
        FileManagement fm = fmService.fetchImageById(imageId);
        byte[] imageContent = fm.getContent();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(fm.getType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@ModelAttribute UserCo user, Model model) {
        model.addAttribute(model);
        return "login";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerSubmit(@ModelAttribute UserOp user, @ModelAttribute PasswordOp password, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("password", password);
        ApplicationResponse appResponse = userManagementService.addUserToOpenproject(user, password);
        String redirectToPage = "";
        
        if (appResponse.getCode() == BasicResponseCode.SUCCESS) {
            model.addAttribute("new_registration", appResponse.getMessage());
            return login(model);
        } else {
            redirectToPage = "registration";
            model.addAttribute("error", appResponse.getMessage());
        }
        logger.log(Level.INFO, "StatusCode: {0} StatusMessage: {1}", new Object[]{appResponse.getCode(), appResponse.getMessage()});
        return redirectToPage;
        
    }
    
    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public String dashboardSubmit(Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
        model.addAttribute("projectID", projectID);
        return "redirect:/" + dashboard(model);
    }
    
    @RequestMapping(value = "/filemanagement/{project_id}", method = RequestMethod.GET)
    public String fetchFilesByProjectID(Model model, @PathVariable int project_id) {
        System.out.println("I am in....!");
        System.out.println("ProjectID: " + project_id);
        System.out.println("Total records: " + fmService.fetchImagesByProjectIdAndUserId(project_id, WebController.getCurrentUser().getRole()).size());
        model.addAttribute("fmContents", fmService.fetchImagesByProjectIdAndUserId(project_id, WebController.getCurrentUser().getRole()));
        model.addAttribute("maxFiles", "1024");
        return "fm :: fmContentList";
        
    }
    
    @RequestMapping(value = "/fm", method = RequestMethod.GET)
    public String fmPage(Model model) {
        return "fm";
    }

    /*
     *  Help Methods
     */
    /**
     * Retrieve the current logged-in user
     *
     * @return An instance of CurrentUser object
     */
    public static CurrentUser getCurrentUser() {
        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
