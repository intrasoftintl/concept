package eu.concept.controller;

import eu.concept.authentication.CurrentUser;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.MetadataService;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.repository.openproject.service.UserManagementOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    MetadataService metadataService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        if (isUserLoggedIn()) {
            return "redirect:/" + dashboard(model);
        }
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new UserCo());
        if (isUserLoggedIn()) {
            return "redirect:/" + dashboard(model);
        }
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
        logger.log(Level.INFO, "Success login for user: {0} , with userID: {1} and role: {2}", new Object[]{getCurrentUser().getUsername(), getCurrentUser().getId(), getCurrentUser().getRole()});

        System.out.println("Project ID is : " + model.asMap().get("projectID"));
        if (!model.containsAttribute("projectID")) {
            model.addAttribute("projectID", "0");
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
    
    // Project Management
    @RequestMapping(value = "/pm_app", method = RequestMethod.GET)
    public String pm(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "pm_app";
    }

    // Preferences
    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public String preferences(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "preferences";
    }

    // Search Engine ALL
    @RequestMapping(value = "/se_all", method = RequestMethod.GET)
    public String se_all(Model model) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "se_all";
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
        return dashboard(model);
    }

    //Change user password
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String changePassword(Model model, @RequestParam(value = "currentPassword", defaultValue = "", required = false) String currentPassword, @RequestParam(value = "newPassword", defaultValue = "", required = false) String newPassword, final RedirectAttributes redirectAttributes) {
        ApplicationResponse appResponse = userManagementService.changeUserPassword(getCurrentUser().getId(), currentPassword, newPassword);
        redirectAttributes.addFlashAttribute("cpResponse", appResponse);
        logger.log(Level.INFO, "{0}:  {1}", new Object[]{appResponse.getCode(), appResponse.getMessage()});
        return "redirect:/preferences";
    }

//    @Autowired
//    SimpMessagingTemplate template;
//
//    @RequestMapping(value = "/dummy", method = RequestMethod.GET)
//    public String greet(String greeting) {
//        String text = "[Debug]:" + greeting;
//        this.template.convertAndSend("/topic/project1", text);
//        return "dashboard";
//    }

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

    /**
     * Get current logged-in role
     *
     * @return Role name
     */
    public static String getCurrentRole() {
        return getCurrentUser().getRole();
    }

    /**
     * Check if a user is logged-in to the COnCEPT Platform
     *
     * @return True if user is logged-in, otherwise returns false
     */
    private boolean isUserLoggedIn() {
        try {
            getCurrentUser();
        } catch (ClassCastException ex) {
            return false;
        }
        return true;
    }

    /**
     * Retrieve the current logged-in user (as a UserCo object)
     *
     * @return An instance of UserCo
     */
    public static UserCo getCurrentUserCo() {
        UserCo currentUserCo;
        currentUserCo = new UserCo();
        currentUserCo.setId(getCurrentUser().getId());
        return currentUserCo;
    }

}
