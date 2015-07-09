package eu.concept.controller;

import eu.concept.authentication.CurrentUser;
import eu.concept.repository.concept.domain.UserCo;
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

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        //return "redirect:/login";
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new UserCo());
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model model) {
        model.addAttribute("user", new UserOp());
        model.addAttribute("password", new PasswordOp());
        return "registration";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        logger.log(Level.INFO, "Success login for user: {0} , with userID: {1} and role: {2}", new Object[]{getCurrentUser().getUsername(), getCurrentUser().getId(), getCurrentUser().getRole()});
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        System.out.println("Projects size: " + projects.size());
        return "dashboard";
    }

    // Notifications
    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public String notifications() {
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
    public String fm_app() {
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

    /*
     *  Help Methods
     */
    /**
     * Retrieve the current logged-in user
     *
     * @return An instance of CurrentUser object
     */
    private static CurrentUser getCurrentUser() {
        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
