package eu.concept.controller.component;

import eu.concept.controller.WebController;
import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.dao.BACommentRepository;
import eu.concept.repository.concept.dao.FMCommentRepository;
import eu.concept.repository.concept.dao.MBCommentRepository;
import eu.concept.repository.concept.dao.MMCommentRepository;
import eu.concept.repository.concept.dao.SBCommentRepository;
import eu.concept.repository.concept.domain.BAComment;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.FMComment;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.domain.MBComment;
import eu.concept.repository.concept.domain.MMComment;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.domain.Moodboard;
import eu.concept.repository.concept.domain.SBComment;
import eu.concept.repository.concept.domain.Storyboard;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.MindMapService;
import eu.concept.repository.concept.service.MoodboardService;
import eu.concept.repository.concept.service.StoryboardService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentController {

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    BriefAnalysisService baService;

    @Autowired
    BACommentRepository baCommentsRepo;

    @Autowired
    FileManagementService fmService;

    @Autowired
    FMCommentRepository fmCommentsRepo;

    @Autowired
    MMCommentRepository mmCommentsRepo;

    @Autowired
    MindMapService mmService;

    @Autowired
    MBCommentRepository mbCommentsRepo;

    @Autowired
    MoodboardService mbService;

    @Autowired
    SBCommentRepository sbCommentsRepo;

    @Autowired
    StoryboardService sbService;

    private final Logger logger = Logger.getLogger(CommentController.class.getName());

    //Comments for BriefAnalysis Application
    @RequestMapping(value = "/ba_all/{pid}/{ba_id}", method = RequestMethod.GET)
    public String baComments(Model model, @PathVariable int pid, @PathVariable int ba_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        BriefAnalysis briefAnalysis = baService.fetchBriefAnalysisById(ba_id);
        //Check if BriefAnalysis exists
        if (null == briefAnalysis) {
            logger.severe("BriefAnalysis with Id: " + ba_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", briefAnalysis);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("app", "ba_app");
        return "comment_app";
    }

    //Comments for FileManagement Application
    @RequestMapping(value = "/fm_all/{pid}/{fm_id}", method = RequestMethod.GET)
    public String fmComments(Model model, @PathVariable int pid, @PathVariable int fm_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        FileManagement filemanagement = fmService.fetchImageById(fm_id);
        //Check if BriefAnalysis exists
        if (null == filemanagement) {
            logger.severe("FileManagement with Id: " + fm_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", filemanagement);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("app", "fm_app");
        return "comment_app";
    }

    //Comments for MindMap Application
    @RequestMapping(value = "/mm_all/{pid}/{mm_id}", method = RequestMethod.GET)
    public String mmComments(Model model, @PathVariable int pid, @PathVariable int mm_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        MindMap mindmap = mmService.fetchMindMapById(mm_id);
        //Check if BriefAnalysis exists
        if (null == mindmap) {
            logger.severe("MindMap with Id: " + mm_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", mindmap);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("app", "mm_app");
        return "comment_app";
    }

    //Comments for MoodBoard Application
    @RequestMapping(value = "/mb_all/{pid}/{mb_id}", method = RequestMethod.GET)
    public String mbComments(Model model, @PathVariable int pid, @PathVariable int mb_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        Moodboard moodboard = mbService.fetchMoodboardById(mb_id);
        //Check if BriefAnalysis exists
        if (null == moodboard) {
            logger.severe("MindMap with Id: " + mb_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", moodboard);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("app", "mb_app");
        return "comment_app";
    }

    //Comments for MoodBoard Application
    @RequestMapping(value = "/sb_all/{pid}/{sb_id}", method = RequestMethod.GET)
    public String sbComments(Model model, @PathVariable int pid, @PathVariable int sb_id) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        Storyboard storyboard = sbService.fetchStoryboardById(sb_id);
        //Check if BriefAnalysis exists
        if (null == storyboard) {
            logger.severe("MindMap with Id: " + sb_id + " could not be found...");
        }
        model.addAttribute("projectID", pid);
        model.addAttribute("item", storyboard);
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("app", "sb_app");
        return "comment_app";
    }

    @RequestMapping(value = "/comments_add", method = RequestMethod.GET)
    public String addComment(Model model, @RequestParam(value = "projectID", defaultValue = "0") int projectID, @RequestParam(value = "itemID", defaultValue = "0") int itemID, @RequestParam(value = "app", defaultValue = "") String app, @RequestParam(value = "content", defaultValue = "") String content) {
        System.out.println("Adding comment for item with ID: " + itemID + " and app: " + app + " and user: " + WebController.getCurrentUserCo().getId());

        switch (app) {
            //BriefAnalysis Application
            case "ba_app": {
                BAComment commentBA = new BAComment(WebController.getCurrentUserCo(), new BriefAnalysis(itemID), content);
                baCommentsRepo.save(commentBA);
                break;
            }

            //FileManagement Application
            case "fm_app": {
                FMComment commentFM = new FMComment(WebController.getCurrentUserCo(), new FileManagement(itemID), content);
                fmCommentsRepo.save(commentFM);
                break;
            }

            //MindMap Application
            case "mm_app": {
                MMComment commentMM = new MMComment(WebController.getCurrentUserCo(), new MindMap(itemID), content);
                mmCommentsRepo.save(commentMM);
                break;
            }

            //Moodboard Application
            case "mb_app": {
                MBComment commentMB = new MBComment(WebController.getCurrentUserCo(), new Moodboard(itemID), content);
                mbCommentsRepo.save(commentMB);
                break;
            }

            //Storyboard Application
            case "sb_app": {
                SBComment commentSB = new SBComment(WebController.getCurrentUserCo(), new Storyboard(itemID), content);
                sbCommentsRepo.save(commentSB);
                break;
            }
        }
        return "redirect:/" + app.replace("app", "all") + "/" + projectID + "/" + itemID;
    }

}
