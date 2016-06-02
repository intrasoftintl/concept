package eu.concept.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.concept.authentication.CurrentUser;
import static eu.concept.main.SemanticAnnotator.getTagsForImage;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.domain.Tag;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.MetadataService;
import eu.concept.repository.concept.service.TagService;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import eu.concept.repository.openproject.domain.PasswordOp;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.domain.UserOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.repository.openproject.service.UserManagementOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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

    @Autowired
    TagService tagService;

    @Autowired
    FileManagementService fmService;

    @Autowired
    ElasticSearchController elasticSearchController;


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
        model.addAttribute("userrole", new MemberRoleOp());
        return "registration";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        logger.log(Level.INFO, "Success login for user: {0} , with userID: {1} and role: {2}", new Object[]{getCurrentUser().getUsername(), getCurrentUser().getId(), getCurrentUser().getRole()});
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

    // Timeline
    @RequestMapping(value = "/timeline_app", method = RequestMethod.POST)
    public String timeline(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model, @RequestParam(value = "limit", defaultValue = "200", required = false) int limit) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "timeline_app";
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
    public String registerSubmit(@ModelAttribute UserOp user, @ModelAttribute PasswordOp password, @ModelAttribute MemberRoleOp userrole, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("password", password);
        model.addAttribute("userrole", userrole);
        System.out.println("User role: " + userrole.getRoleId());

        ApplicationResponse appResponse = userManagementService.addUserToOpenproject(user, password, userrole);
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

    // Tags
    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public String tags(Model model, @RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {

        Tag tag = tagService.fetchTagByPid(projectID);

        if (null == tag) {
            model.addAttribute("showTagsAlert", "true");
            tag = new Tag();
            tag.setPid(projectID);
        } else {
            model.addAttribute("showTagsAlert", "false");
        }

        model.addAttribute("tag", tag);
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        return "tags_app";
    }

    @RequestMapping(value = "/tagsAdd", method = RequestMethod.POST)
    public String tagsSubmit(@ModelAttribute Tag tag, Model model) {

        tag.setUid(getCurrentUserCo());
        if (tagService.store(tag)) {
            tag.getTags().forEach(keyword -> CompletableFuture.supplyAsync(() -> {
                try {
                    HttpResponse<JsonNode> jsonResponse = Unirest.get("https://www.europeana.eu/api/v2/search.json?wskey=wNrgTqaJw&query=" + keyword + "&qf=IMAGE_SIZE:extra_large&qf=MIME_TYPE:image%2Fjpeg").asJson();

                    // System.out.println("Query is: https://www.europeana.eu/api/v2/search.json?wskey=wNrgTqaJw&query=" + keyword + "&qf=IMAGE_SIZE:extra_large&qf=MIME_TYPE:image%2Fjpeg");
                    jsonResponse.getBody().getObject().getJSONArray("items").forEach(item -> {

                        String url = ((JSONObject) item).getJSONArray("edmPreview").get(0).toString();
                        //System.out.println("Iamge url: " + url);

                        InputStream in;
                        try {
                            in = new BufferedInputStream(new URL(url).openStream());

                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            int n = 0;

                            while (-1 != (n = in.read(buf))) {
                                out.write(buf, 0, n);
                            }

                            out.close();
                            in.close();
                            byte[] imageBytes = out.toByteArray();

                            String imageBase64 = "data:image/jpeg;base64,".concat(Base64.getEncoder().encodeToString(imageBytes));

                            //Save Image
                            FileManagement file = new FileManagement(null, tag.getPid(), keyword.concat(".jpg"), imageBase64, "image/jpeg", Short.parseShort("1"), null);
                            file.setUid(tag.getUid());
                            if (fmService.storeFile(file)) {
                                //System.out.println("Image saved!");
                                String keywords = getTagsForImage("file/" + String.valueOf(file.getId()));
                                Optional<Metadata> metadata = Optional.of(new Metadata(null, file.getId(), "{\"open_nodes\":[],\"selected_node\":[]}", keywords, "", null));
                                metadata.get().setComponent(new Component("FM"));
                                metadataService.storeMetadata(metadata.get());
                                //Insert document to elastic search engine            
                                elasticSearchController.insert(Optional.ofNullable(file), metadata);
                            } else {
                                System.out.println("Could not fetch image..");
                            }

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    );

                } catch (UnirestException ex) {
                    Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
            }));

        }

        model.addAttribute("projectID", tag.getPid());
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());

        return "redirect:/dashboard";

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
